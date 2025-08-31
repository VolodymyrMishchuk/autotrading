--liquibase formatted sql
--changeset autotrade:014-user-action-audit

CREATE TABLE IF NOT EXISTS user_action_audit (
    id          UUID PRIMARY KEY,
    user_id     UUID REFERENCES users(id) ON DELETE SET NULL,
    action      VARCHAR(50) NOT NULL,
    channel     VARCHAR(16),
    target      VARCHAR(255),
    ip          VARCHAR(64),
    user_agent  TEXT,
    success     BOOLEAN NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_uad_user ON user_action_audit(user_id);