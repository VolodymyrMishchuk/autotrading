--liquibase formatted sql

--changeset volodymyr.mishchuk:013-drop-users-token-column
ALTER TABLE "users" DROP COLUMN IF EXISTS "token";

--rollback ALTER TABLE "users" ADD COLUMN "token" UUID;