--liquibase formatted sql

--changeset volodymyr.mishchuk:012-migrate-email-verification-to-uats runInTransaction:true

INSERT INTO "user_action_tokens" (id, token, purpose, channel, data, expires_at, consumed_at, created_at, user_id)
SELECT evt.id,
       evt.token,
       'EMAIL_VERIFICATION'::VARCHAR(32)
       AS purpose, 'EMAIL'::VARCHAR(16)
       AS channel, NULL::JSONB
       AS data, evt.expires_at,
       evt.confirmed_at AS consumed_at,
       evt.created_at,
       evt.user_id
FROM "email_verification_tokens" evt;

DROP TABLE IF EXISTS "email_verification_tokens";

--rollback CREATE TABLE "email_verification_tokens"
--rollback (
--rollback     "id"           UUID PRIMARY KEY,
--rollback     "token"        VARCHAR(255) NOT NULL UNIQUE,
--rollback     "expires_at"   TIMESTAMPTZ  NOT NULL,
--rollback     "confirmed_at" TIMESTAMPTZ,
--rollback     "created_at"   TIMESTAMPTZ  NOT NULL,
--rollback     "user_id"      UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE
--rollback );
--rollback DELETE FROM "user_action_tokens" WHERE purpose = 'EMAIL_VERIFICATION';