--liquibase formatted sql

--changeset volodymyr.mishchuk:9

ALTER TABLE "users"
ALTER COLUMN "role" TYPE VARCHAR(25);

--rollback ALTER TABLE "users" ALTER COLUMN "role" TYPE VARCHAR(15);