from __future__ import annotations

from typing import Literal, Any

from pydantic import BaseModel, Field

Direction = Literal["BUY", "SELL"]
SignalType = Literal["OPEN", "CLOSE"]

class SessionStartReq(BaseModel):
    name: str
    api_id: int
    api_hash: str
    phone: str

class SessionConfirmCodeReq(BaseModel):
    name: str
    code: str

class SessionConfirmPasswordReq(BaseModel):
    name: str
    password: str

class SourceCreateReq(BaseModel):
    source_id: str
    session: str
    chat: str | int = Field(..., description="@username | invite link | numeric id")
    settings: dict[str, Any] | None = None

class SourceDeleteReq(BaseModel):
    source_id: str

class SignalEvent(BaseModel):
    source_id: str
    type: SignalType
    symbol: str
    direction: Direction | None = None
    amount: float | None = None
    chat_id: int
    message_id: int
    date_ts: int
    raw_text: str