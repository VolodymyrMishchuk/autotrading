--liquibase formatted sql
--changeset autotrade:016-extend-raw-signals

-- 0) UUID генерація (PostgreSQL)
-- gen_random_uuid() надає розширення pgcrypto
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 1) Міняємо BIGSERIAL id на UUID (таблиця пуста)
ALTER TABLE "raw_signals"
DROP CONSTRAINT IF EXISTS "raw_signals_pkey";

ALTER TABLE "raw_signals"
DROP COLUMN IF EXISTS "id";

ALTER TABLE "raw_signals"
    ADD COLUMN "id" UUID NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE "raw_signals"
    ADD CONSTRAINT "raw_signals_pkey" PRIMARY KEY ("id");


-- 2) Розширюємо таблицю додатковими колонками

ALTER TABLE "raw_signals"
    ADD COLUMN IF NOT EXISTS "chat_id"         BIGINT,
    ADD COLUMN IF NOT EXISTS "chat_title"      TEXT,
    ADD COLUMN IF NOT EXISTS "message_id"      BIGINT,
    ADD COLUMN IF NOT EXISTS "sender_id"       BIGINT,
    ADD COLUMN IF NOT EXISTS "msg_date"        TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS "edit_date"       TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS "is_edit"         BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS "version"         INT NOT NULL DEFAULT 1,
    ADD COLUMN IF NOT EXISTS "kafka_topic"     VARCHAR(128),
    ADD COLUMN IF NOT EXISTS "kafka_partition" INT,
    ADD COLUMN IF NOT EXISTS "kafka_offset"    BIGINT,
    ADD COLUMN IF NOT EXISTS "raw_json"        JSONB;

-- 3) Унікальний індекс на версію в межах повідомлення

CREATE UNIQUE INDEX IF NOT EXISTS "uq_raw_signals_chat_msg_ver"
    ON "raw_signals" ("chat_id", "message_id", "version");

-- 4) Корисні індекси

CREATE INDEX IF NOT EXISTS "idx_raw_signals_msg_date"
    ON "raw_signals" ("msg_date");

CREATE INDEX IF NOT EXISTS "idx_raw_signals_chat_msg"
    ON "raw_signals" ("chat_id", "message_id");
