--liquibase formatted sql

--changeset volodymyr.mishchuk:011-create-user-action-tokens

CREATE TABLE IF NOT EXISTS "user_action_tokens"
(
    "id"          UUID PRIMARY KEY,
    "token"       VARCHAR(255) NOT NULL UNIQUE,
    "purpose"     VARCHAR(32)  NOT NULL,
    "channel"     VARCHAR(16)  NOT NULL,
    "data"        JSONB,
    "expires_at"  TIMESTAMPTZ  NOT NULL,
    "consumed_at" TIMESTAMPTZ,
    "created_at"  TIMESTAMPTZ  NOT NULL,
    "user_id"     UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS "idx_uat_user_purpose" ON "user_action_tokens" ("user_id","purpose");

--rollback DROP TABLE IF EXISTS "user_action_tokens";