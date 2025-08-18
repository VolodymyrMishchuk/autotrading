import os
import logging
from pathlib import Path
from contextlib import asynccontextmanager
from typing import Any

from fastapi import FastAPI, HTTPException
from starlette.middleware.cors import CORSMiddleware

from .models import SessionStartReq, SourceCreateReq, SourceDeleteReq
from .manager import TelethonManager

logging.basicConfig(
    level=os.getenv("LOG_LEVEL", "INFO"),
    format="%(asctime)s %(levelname)s %(name)s: %(message)s",
)
log = logging.getLogger("telethon-ingest")

SESSIONS_DIR = Path(os.getenv("SESSIONS_DIR", "./sessions"))
KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
KAFKA_TOPIC = os.getenv("KAFKA_TOPIC", "signals.raw")

KAFKA_SASL_MECH = os.getenv("KAFKA_SASL_MECHANISM", "").strip()
KAFKA_USERNAME = os.getenv("KAFKA_USERNAME", "").strip()
KAFKA_PASSWORD = os.getenv("KAFKA_PASSWORD", "").strip()
KAFKA_SECURITY_PROTOCOL = os.getenv("KAFKA_SECURITY_PROTOCOL", "").strip()
KAFKA_SSL_CAFILE = os.getenv("KAFKA_SSL_CAFILE", "").strip()

kafka_sasl: dict[str, Any] | None = None
if KAFKA_SASL_MECH:
    kafka_sasl = {
        "mechanism": KAFKA_SASL_MECH,
        "username": KAFKA_USERNAME,
        "password": KAFKA_PASSWORD,
        "security_protocol": KAFKA_SECURITY_PROTOCOL or ("SASL_SSL" if KAFKA_SSL_CAFILE else "SASL_PLAINTEXT"),
        "ssl_cafile": KAFKA_SSL_CAFILE or None,
    }

mgr = TelethonManager(
    sessions_dir=SESSIONS_DIR,
    kafka_bootstrap=KAFKA_BOOTSTRAP,
    kafka_topic=KAFKA_TOPIC,
    kafka_sasl=kafka_sasl,
)

@asynccontextmanager
async def lifespan(_: FastAPI):
    SESSIONS_DIR.mkdir(parents=True, exist_ok=True)
    if hasattr(mgr, "startup"):
        await mgr.startup()
    log.info("Telethon ingest service started")
    try:
        yield
    finally:
        if hasattr(mgr, "shutdown"):
            await mgr.shutdown()
        log.info("Telethon ingest service stopped")

app = FastAPI(title="Telegram Ingest (Telethon + Kafka)", lifespan=lifespan)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_headers=["*"],
    allow_methods=["*"],
    allow_credentials=True,
)

@app.get("/health")
async def health() -> dict[str, bool]:
    return {"ok": True}

@app.post("/sessions/start")
async def sessions_start(body: SessionStartReq) -> dict[str, str]:
    status = await mgr.start_session(body.name, body.api_id, body.api_hash, body.phone)
    return {"status": status}

@app.post("/sessions/confirm")
async def sessions_confirm(body: dict) -> dict[str, str]:
    name = body.get("name")
    if not name:
        raise HTTPException(400, "Field 'name' is required")

    phone = body.get("phone")
    code = body.get("code")
    password = body.get("password")

    if code:
        try:
            status = await mgr.confirm_code(name, phone, code)
            return {"status": status}
        except Exception as e:
            raise HTTPException(400, str(e))
    if password:
        status = await mgr.confirm_password(name, password)
        return {"status": status}

    raise HTTPException(400, "Need 'code' or 'password'")

@app.post("/sessions/stop/{name}")
async def sessions_stop(name: str) -> dict[str, str]:
    await mgr.stop_session(name)
    return {"status": "stopped"}

@app.post("/sources")
async def sources_add(body: SourceCreateReq) -> dict[str, int] | dict[str, str]:
    return await mgr.add_source(body.source_id, body.session, body.chat, body.settings or {})

@app.delete("/sources")
async def sources_delete(body: SourceDeleteReq) -> dict[str, str]:
    return {"status": await mgr.remove_source(body.source_id)}
