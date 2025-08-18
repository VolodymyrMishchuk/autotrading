import re
from typing import Any

_DEFAULT_OPEN = r"\b(BUY|SELL)\s+([A-Z0-9_\-\.]+)(?:\s+(\d+(?:\.\d+)?))?\b"
_DEFAULT_CLOSE = r"\bCLOSE\s+([A-Z0-9_\-\.]+)\b"

class SignalParser:
    def __init__(self, settings: dict[str, Any] | None = None):
        s = settings or {}
        self.open_re = re.compile(s.get("open_re", _DEFAULT_OPEN), re.IGNORECASE)
        self.close_re = re.compile(s.get("close_re", _DEFAULT_CLOSE), re.IGNORECASE)
        self.symbol_map: dict[str, str] = {k.upper(): v for k, v in (s.get("symbol_map") or {}).items()}

        # default groups for our patterns
        self.g_open_dir = int(s.get("g_open_dir", 1))
        self.g_open_sym = int(s.get("g_open_sym", 2))
        self.g_open_amount = int(s.get("g_open_amount", 3))
        self.g_close_sym = int(s.get("g_close_sym", 1))

    def _map_sym(self, sym: str) -> str:
        up = sym.upper()
        return self.symbol_map.get(up, up)

    def parse(self, text: str) -> dict[str, Any] | None:
        t = text.strip()

        m = self.open_re.search(t)
        if m:
            direction = m.group(self.g_open_dir).upper()
            symbol = self._map_sym(m.group(self.g_open_sym))
            amount: float | None = None
            g = m.group(self.g_open_amount)
            if g:
                try:
                    amount = float(g)
                except Exception:
                    pass
            return {"type": "OPEN", "direction": direction, "symbol": symbol, "amount": amount}

        m = self.close_re.search(t)
        if m:
            symbol = self._map_sym(m.group(self.g_close_sym))
            return {"type": "CLOSE", "symbol": symbol}

        return None