--liquibase formatted sql

--changeset volodymyr.mishchuk:1

CREATE TABLE "users"
(
    "id"           UUID PRIMARY KEY,
    "first_name"   VARCHAR(50)  NOT NULL,
    "last_name"    VARCHAR(50)  NOT NULL,
    "birth_day"    DATE         NOT NULL,
    "phone_number" VARCHAR(16)  NOT NULL UNIQUE,
    "email"        VARCHAR(255) NOT NULL UNIQUE,
    "password"     VARCHAR(255),
    "role"         VARCHAR(15)  NOT NULL,
    "status"       VARCHAR(10)  NOT NULL,
    "created_at"   TIMESTAMPTZ  NOT NULL,
    "updated_at"   TIMESTAMPTZ
);

CREATE TABLE "email_verification_tokens"
(
    "id"           UUID PRIMARY KEY,
    "token"        VARCHAR(255) NOT NULL UNIQUE,
    "expires_at"   TIMESTAMPTZ  NOT NULL,
    "confirmed_at" TIMESTAMPTZ,
    "created_at"   TIMESTAMPTZ  NOT NULL,
    "user_id"      UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE "accounts"
(
    "id"                 UUID PRIMARY KEY,
    "name"               VARCHAR(255)   NOT NULL UNIQUE,
    "status"             VARCHAR(10)    NOT NULL,
    "balance"            NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    "token_MetaTradeAPI" TEXT           NOT NULL,
    "created_at"         TIMESTAMPTZ    NOT NULL,
    "updated_at"         TIMESTAMPTZ,
    "user_id"            UUID           NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE "sources"
(
    "id"         UUID PRIMARY KEY,
    "name"       VARCHAR(255) NOT NULL,
    "platform"   VARCHAR(50)  NOT NULL,
    "status"     VARCHAR(10)  NOT NULL,
    "token"      TEXT         NOT NULL,
    "created_at" TIMESTAMPTZ  NOT NULL,
    "updated_at" TIMESTAMPTZ
);

CREATE TABLE "cabinets"
(
    "id"               UUID PRIMARY KEY,
    "name"             VARCHAR(255) NOT NULL,
    "meta_trade_token" TEXT,
    "status"           VARCHAR(10)  NOT NULL,
    "user_id"          UUID         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    "account_id"       UUID         NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    "created_at"       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    "updated_at"       TIMESTAMPTZ
);

CREATE TABLE "cabinet_sources"
(
    "id"         UUID PRIMARY KEY,
    "cabinet_id" UUID        NOT NULL REFERENCES cabinets (id) ON DELETE CASCADE,
    "source_id"  UUID        NOT NULL REFERENCES sources (id) ON DELETE CASCADE,
    "status"     VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE "transactions"
(
    "id"            UUID PRIMARY KEY,
    "symbol"        VARCHAR(20)    NOT NULL,
    "amount"        NUMERIC(15, 2) NOT NULL CHECK (amount >= 0),
    "direction"     VARCHAR(15)    NOT NULL,
    "opened_at"     TIMESTAMPTZ    NOT NULL,
    "closed_at"     TIMESTAMPTZ,
    "balance_after" NUMERIC(15, 2),
    "is_profitable" BOOLEAN,
    "user_id"       UUID           NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    "account_id"    UUID           NOT NULL REFERENCES accounts (id) ON DELETE CASCADE,
    "cabinet_id"    UUID           NOT NULL REFERENCES cabinets (id) ON DELETE CASCADE,
    "source_id"     UUID           NOT NULL REFERENCES sources (id) ON DELETE CASCADE,
    "created_at"    TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);