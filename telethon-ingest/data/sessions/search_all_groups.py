from telethon.sync import TelegramClient

api_id = 26644885
api_hash = "fa9fa42ac92fb807910e7b6005b9aed9"

with TelegramClient("mysession", api_id, api_hash) as client:
    print("✅ Connected to Telegram!")
    print("Fetching dialogs...\n")

    dialogs = client.get_dialogs()
    for d in dialogs:
        print(f"📘 Name: {d.name}\n🆔 ID: {d.id}\nType: {type(d.entity).__name__}\n{'-'*40}")
