--liquibase formatted sql

--changeset volodymyr.mishchuk:2

ALTER TABLE "sources"
    ADD COLUMN "account_id" UUID NOT NULL REFERENCES accounts (id) ON DELETE CASCADE;