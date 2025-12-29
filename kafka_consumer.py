import asyncio
import json
from datetime import datetime
from aiokafka import AIOKafkaConsumer

# === Конфігурація ===
KAFKA_BOOTSTRAP = "localhost:29092"   # якщо запускаєш на Mac поза Docker
TOPIC = "signals.raw"
LOG_FILE = "signals_log.jsonl"

async def consume():
    consumer = AIOKafkaConsumer(
        TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP,
        value_deserializer=lambda v: json.loads(v.decode("utf-8")),
        auto_offset_reset="earliest",   # бачить навіть старі повідомлення
        group_id="debug-consumer"
    )

    await consumer.start()
    print(f"✅ Listening to Kafka topic '{TOPIC}' on {KAFKA_BOOTSTRAP}\n")

    try:
        async for msg in consumer:
            data = msg.value
            chat_id = data.get("chat_id")
            text = data.get("text", "").strip()
            date = data.get("date", "")

            # --- Вивести у консоль ---
            print(f"💬 [{chat_id}] {text}")

            # --- Запис у файл ---
            log_entry = {
                "timestamp": datetime.utcnow().isoformat(),
                "chat_id": chat_id,
                "text": text,
                "date": date
            }
            with open(LOG_FILE, "a", encoding="utf-8") as f:
                f.write(json.dumps(log_entry, ensure_ascii=False) + "\n")

    except KeyboardInterrupt:
        print("\n🛑 Stopped by user.")
    finally:
        await consumer.stop()
        print(f"🧾 All messages saved to {LOG_FILE}")

if __name__ == "__main__":
    asyncio.run(consume())
