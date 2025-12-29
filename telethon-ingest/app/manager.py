from __future__ import annotations

import os
import json
import logging
import asyncio
import threading

from aiokafka import AIOKafkaProducer
from telethon import TelegramClient, events
from telethon.network.connection import (
    ConnectionTcpFull,
    ConnectionTcpAbridged,
    ConnectionTcpObfuscated,
    ConnectionTcpIntermediate,
)

log = logging.getLogger("app.manager")


class TelethonManager:
    def __init__(self, sessions_dir, kafka_bootstrap, kafka_topic, kafka_sasl=None):
        self.sessions_dir = sessions_dir
        self.kafka_bootstrap = kafka_bootstrap
        self.kafka_topic = kafka_topic
        self.kafka_sasl = kafka_sasl

        self.session_path = os.getenv("SESSION_PATH", f"{sessions_dir}/mysession.session")
        self.api_id = int(os.getenv("TELEGRAM_API_ID"))
        self.api_hash = os.getenv("TELEGRAM_API_HASH")

        self.group_ids = [
            int(os.getenv("GROUP_1_ID", "0")),
            int(os.getenv("GROUP_2_ID", "0")),
        ]

        self.client: TelegramClient | None = None
        self.producer: AIOKafkaProducer | None = None
        self._loop: asyncio.AbstractEventLoop | None = None
        self._thread: threading.Thread | None = None

    # === Public lifecycle ===
    async def startup(self):
        """Старт Kafka producer та Telethon listener у фоновому потоці"""
        # Ретраї на випадок, якщо Kafka ще не повністю готова (після healthcheck)
        max_attempts = int(os.getenv("KAFKA_BOOTSTRAP_RETRIES", "10"))
        delay_sec = float(os.getenv("KAFKA_BOOTSTRAP_DELAY", "2"))

        self.producer = AIOKafkaProducer(
            bootstrap_servers=self.kafka_bootstrap,
            value_serializer=lambda v: json.dumps(v, ensure_ascii=False).encode("utf-8"),
        )

        for attempt in range(1, max_attempts + 1):
            try:
                await self.producer.start()
                log.info(f"✅ Kafka producer started ({self.kafka_bootstrap})")
                break
            except Exception as e:
                log.warning(f"Kafka bootstrap failed (try {attempt}/{max_attempts}): {e}")
                if attempt == max_attempts:
                    raise
                await asyncio.sleep(delay_sec)

        # Запускаємо окремий event loop у фоні
        self._thread = threading.Thread(target=self._run_telethon_loop, daemon=True)
        self._thread.start()
        log.info("🚀 Telethon background loop started")

    async def shutdown(self):
        if self.producer:
            await self.producer.stop()
            log.info("🧹 Kafka producer stopped")
        if self.client and self.client.is_connected():
            await self.client.disconnect()
            log.info("🧹 Telegram client disconnected")

    # === Internal ===
    def _run_telethon_loop(self):
        """Окремий asyncio loop для Telethon"""
        self._loop = asyncio.new_event_loop()
        asyncio.set_event_loop(self._loop)
        self._loop.run_until_complete(self._telethon_main())

    def _build_telegram_client(self) -> TelegramClient:
        """
        Створює TelegramClient із параметрами з ENV:
        - TELETHON_CONNECTION = full|abridged|obfuscated|intermediate  (default: obfuscated)
        - TELETHON_PORT       = <int> (default: 443; можна 80)
        - TELETHON_USE_IPV6   = true|false (default: false)
        - TELETHON_CONN_RETRIES = <int> (default: 5)
        - TELETHON_RETRY_DELAY  = <int seconds> (default: 1)
        - PROXY_TYPE = socks5|http
        - PROXY_HOST = <host>
        - PROXY_PORT = <port>
        """
        CONNECTION_MAP = {
            "full": ConnectionTcpFull,
            "abridged": ConnectionTcpAbridged,
            "obfuscated": ConnectionTcpObfuscated,
            "intermediate": ConnectionTcpIntermediate,
        }

        conn_name = os.getenv("TELETHON_CONNECTION", "obfuscated").lower()
        base_conn = CONNECTION_MAP.get(conn_name, ConnectionTcpObfuscated)

        port_env = int(os.getenv("TELETHON_PORT", "443"))
        # Динамічно задаємо порт для обраного типу конекшену
        CustomConn = type("CustomConn", (base_conn,), {"default_port": port_env})

        use_ipv6 = os.getenv("TELETHON_USE_IPV6", "false").lower() == "true"
        conn_retries = int(os.getenv("TELETHON_CONN_RETRIES", "5"))
        retry_delay = int(os.getenv("TELETHON_RETRY_DELAY", "1"))

        # Опційний простий проксі (Telethon розуміє ('socks5'|'http', host, port))
        proxy = None
        ptype = os.getenv("PROXY_TYPE")
        phost = os.getenv("PROXY_HOST")
        pport = os.getenv("PROXY_PORT")
        if ptype and phost and pport:
            proxy = (ptype, phost, int(pport))

        log.info(
            f"🔧 Telethon connection='{conn_name}' port={port_env} ipv6={use_ipv6} "
            f"retries={conn_retries} retry_delay={retry_delay}s proxy={'on' if proxy else 'off'}"
        )

        return TelegramClient(
            self.session_path,
            self.api_id,
            self.api_hash,
            connection=CustomConn,
            proxy=proxy,
            use_ipv6=use_ipv6,
            connection_retries=conn_retries,
            retry_delay=retry_delay,
            system_version="Linux",
            device_model="Docker",
        )

    async def _telethon_main(self):
        try:
            self.client = self._build_telegram_client()
            await self.client.connect()

            if not await self.client.is_user_authorized():
                log.error("❌ Telegram session not authorized. Run create_session.py inside container.")
                return

            me = await self.client.get_me()
            log.info(f"✅ Connected as {getattr(me, 'first_name', '')} (@{getattr(me, 'username', '')})")

            # Підписка на групи
            any_groups = False
            for gid in self.group_ids:
                if gid != 0:
                    self.client.add_event_handler(self._on_message, events.NewMessage(chats=gid))
                    self.client.add_event_handler(self._on_message_edit, events.MessageEdited(chats=gid))
                    log.info(f"📡 Listening to group ID: {gid}")
                    any_groups = True

            if not any_groups:
                log.warning("⚠️ No GROUP_*_ID provided; nothing to subscribe to.")

            log.info("💤 Waiting for new messages...")
            await self.client.run_until_disconnected()

        except Exception as e:
            log.exception(f"💥 Telethon fatal error: {e}")

    # ---- helpers ----
    async def _serialize_message(self, event):
        """Повертає уніфікований payload для нового/зміненного повідомлення."""
        msg = event.message
        chat = await event.get_chat()

        # Назва: для каналів/груп title, для приватних чатів може бути first_name/username
        chat_title = getattr(chat, "title", None)
        if not chat_title:
            chat_title = getattr(chat, "first_name", None) or getattr(chat, "username", None)

        payload = {
            "chat_id": event.chat_id,
            "chat_title": chat_title,
            "message_id": getattr(msg, "id", None),
            "sender_id": event.sender_id,
            "text": (msg.message or "").strip() if msg and msg.message else "",
            "date": str(msg.date) if getattr(msg, "date", None) else None,
            "edit_date": str(getattr(msg, "edit_date", None)) if getattr(msg, "edit_date", None) else None,
        }
        return payload

    async def _send_to_kafka(self, payload: dict):
        if not payload.get("text"):
            return
        if self.producer:
            await self.producer.send_and_wait(self.kafka_topic, payload)
            log.info(
                "💬 [%s] #%s %s → Kafka(%s)",
                payload.get("chat_id"),
                payload.get("message_id"),
                "edit" if payload.get("edit_date") else "new",
                self.kafka_topic,
            )
        else:
            log.warning("⚠️ Kafka producer not initialized yet")

    # ---- handlers ----
    async def _on_message(self, event):
        """Нове повідомлення з Telegram → Kafka"""
        try:
            payload = await self._serialize_message(event)
            await self._send_to_kafka(payload)
        except Exception as e:
            log.error(f"⚠️ Error sending NEW to Kafka: {e}")

    async def _on_message_edit(self, event):
        """Редаговане повідомлення з Telegram → Kafka"""
        try:
            payload = await self._serialize_message(event)
            await self._send_to_kafka(payload)
        except Exception as e:
            log.error(f"⚠️ Error sending EDIT to Kafka: {e}")
