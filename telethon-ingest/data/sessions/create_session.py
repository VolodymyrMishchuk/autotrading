from telethon import TelegramClient

api_id = 26644885
api_hash = "fa9fa42ac92fb807910e7b6005b9aed9"

with TelegramClient("mysession", api_id, api_hash) as client:
    print("✅ Session created")
