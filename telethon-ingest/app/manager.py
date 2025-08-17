import json
import logging
from pathlib import Path
from typing import Any

from aiokafka import AIOKafkaProducer
from telethon import TelegramClient, events
from telethon.errors import SessionPasswordNeededError

from .parser import SignalParser

log = logging.getLogger(__name__)

class TelethonManager:
    def __init__(
            self,
            sessions_dir: Path,
            kafka_bootstrap: str,
            kafka_topic: str,
            kafka_sasl: dict[str, Any] | None = None,
    ):
        self.sessions_dir = sessions_dir
        self.sessions_dir.mkdir(parents=True, exist_ok=True)

        self.kafka_bootstrap = kafka_bootstrap
        self.kafka_topic = kafka_topic
        self.kafka_sasl = kafka_sasl or {}

        self.clients: dict[str, TelegramClient] = {}
        self.sources: dict[str, dict[str, Any]] = {}

        self._producer: AIOKafkaProducer | None = None
        self._producer_started = False

    async def startup(self) -> None:
        try:
            await self.start_kafka()
        except Exception:
            log.warning("Kafka is not ready yet, will lazy-start on first publish", exc_info=True)

    async def shutdown(self) -> None:
        await self.stop_kafka()
        for name, client in list(self.clients.items()):
            try:
                await client.disconnect()
            except Exception:
                pass
        self.clients.clear()

    async def start_kafka(self) -> None:
        if self._producer_started:
            return

        kwargs: dict[str, Any] = {"bootstrap_servers": self.kafka_bootstrap}
        mech = self.kafka_sasl.get("mechanism")
        if mech:
            kwargs.update(
                security_protocol=self.kafka_sasl.get("security_protocol", "SASL_SSL"),
                sasl_mechanism=mech,
                sasl_plain_username=self.kafka_sasl.get("username"),
                sasl_plain_password=self.kafka_sasl.get("password"),
                ssl_cafile=self.kafka_sasl.get("ssl_cafile"),
            )

        self._producer = AIOKafkaProducer(
            value_serializer=lambda v: json.dumps(v, ensure_ascii=False).encode("utf-8"),
            **kwargs,
        )
        await self._producer.start()
        self._producer_started = True
        log.info("Kafka producer started (%s)", self.kafka_bootstrap)

    async def stop_kafka(self) -> None:
        if self._producer and self._producer_started:
            await self._producer.stop()
            self._producer_started = False
            log.info("Kafka producer stopped")

    async def _publish(self, payload: dict[str, Any]) -> None:
        if not self._producer_started:
            await self.start_kafka()
        assert self._producer
        await self._producer.send_and_wait(self.kafka_topic, payload)

    async def start_session(self, name: str, api_id: int, api_hash: str, phone: str) -> str:
        if name in self.clients:
            return "already-running"

        session_path = self.sessions_dir / f"{name}.session"
        client = TelegramClient(str(session_path), api_id, api_hash)

        await client.connect()
        if not await client.is_user_authorized():
            try:
                await client.send_code_request(phone)
                self.clients[name] = client
                return "code-sent"
            except Exception as e:
                await client.disconnect()
                raise e
        else:
            self.clients[name] = client
            return "ready"

    async def confirm_code(self, name: str, phone: str | None, code: str) -> str:
        client = self.clients.get(name)
        if not client:
            raise RuntimeError("session not started")
        try:
            await client.sign_in(phone=phone, code=code)
            return "ready"
        except SessionPasswordNeededError:
            return "password-required"

    async def confirm_password(self, name: str, password: str) -> str:
        client = self.clients.get(name)
        if not client:
            raise RuntimeError("session not started")
        await client.sign_in(password=password)
        return "ready"

    async def stop_session(self, name: str) -> None:
        client = self.clients.pop(name, None)
        if client:
            await client.disconnect()

    async def _resolve_chat_id(self, client: TelegramClient, chat: str | int) -> int:
        if isinstance(chat, int):
            return chat
        entity = await client.get_entity(chat)
        return int(getattr(entity, "id"))

    async def add_source(
            self,
            source_id: str,
            session: str,
            chat: str | int,
            settings: dict[str, Any] | None,
    ) -> dict[str, Any]:
        if source_id in self.sources:
            raise RuntimeError("source already exists")

        client = self.clients.get(session)
        if not client:
            raise RuntimeError("session not running")

        await self.start_kafka()

        chat_id = await self._resolve_chat_id(client, chat)
        parser = SignalParser(settings)

        async def _handler(event):
            try:
                text: str = event.message.message or ""
                parsed = parser.parse(text)
                if not parsed:
                    return

                payload: dict[str, Any] = {
                    "source_id": source_id,
                    "type": parsed["type"],
                    "symbol": parsed.get("symbol"),
                    "direction": parsed.get("direction"),
                    "amount": parsed.get("amount"),
                    "chat_id": chat_id,
                    "message_id": event.message.id,
                    "date_ts": int(event.message.date.timestamp()),
                    "raw_text": text,
                }
                await self._publish(payload)
            except Exception:
                log.exception("Source %s handler error", source_id)

        client.add_event_handler(_handler, events.NewMessage(chats=[chat_id]))

        self.sources[source_id] = {
            "session": session,
            "chat_id": chat_id,
            "handler": _handler,
            "parser": parser,
        }
        return {"source_id": source_id, "chat_id": chat_id}

    async def remove_source(self, source_id: str) -> str:
        src = self.sources.pop(source_id, None)
        if not src:
            return "not-found"
        client = self.clients.get(src["session"])
        if client:
            client.remove_event_handler(src["handler"], events.NewMessage(chats=[src["chat_id"]]))
        return "removed"