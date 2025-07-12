--liquibase formatted sql

--changeset volodymyr.mishchuk:1

CREATE TABLE "users" (
    "id" UUID PRIMARY KEY,
    "first_name" VARCHAR(50) NOT NULL,
    "last_name" VARCHAR(50) NOT NULL,
    "birth_day" DATE NOT NULL,
    "phone_number" VARCHAR(16) NOT NULL UNIQUE,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "password" VARCHAR(255),
    "role" VARCHAR(15) NOT NULL,
    "status" VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL,
    "updated_at" TIMESTAMPTZ,
    "token" UUID
);

CREATE TABLE "accounts" (
    "id" UUID PRIMARY KEY,
    "number" BIGINT NOT NULL UNIQUE,
    "status" VARCHAR(10) NOT NULL,
    "balance" NUMERIC(15,2) NOT NULL DEFAULT 0::money CHECK (balance >= 0::money),
    "currency" CHAR(3) NOT NULL,
    "token_MetaTradeAPI" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL,
    "updated_at" TIMESTAMPTZ,
    "user_id" UUID NOT NULL REFERENCES users(id)
);

CREATE TABLE "sources" (
    "id" UUID PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "platform" VARCHAR(50) NOT NULL,
    "status" VARCHAR(10) NOT NULL,
    "token" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL,
    "updated_at" TIMESTAMPTZ
);

CREATE TABLE "transactions" (
    "id" UUID PRIMARY KEY,
    "amount" NUMERIC(15,2) NOT NULL DEFAULT 0::money CHECK (amount >= 0::money),
    "direction" VARCHAR(15) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL,
    "account_id" UUID NOT NULL REFERENCES Accounts(id),
    "source_id" UUID NOT NULL REFERENCES Sources(id)
);