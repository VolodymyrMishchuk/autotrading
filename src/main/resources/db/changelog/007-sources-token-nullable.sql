--liquibase formatted sql

--changeset volodymyr.mishchuk:007-sources-token-nullable

ALTER TABLE sources
    ALTER COLUMN token DROP NOT NULL;

--rollback UPDATE sources SET token = '' WHERE token IS NULL;
--rollback ALTER TABLE sources ALTER COLUMN token SET NOT NULL;
