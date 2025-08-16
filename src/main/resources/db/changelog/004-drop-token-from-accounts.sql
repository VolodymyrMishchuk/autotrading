--liquibase formatted sql

--changeset volodymyr.mishchuk:004-drop-token-from-accounts

ALTER TABLE accounts DROP COLUMN IF EXISTS token_meta_tradeapi;

--rollback ALTER TABLE accounts ADD COLUMN IF NOT EXISTS token_meta_tradeapi TEXT;
--rollback UPDATE accounts SET token_meta_tradeapi = '' WHERE token_meta_tradeapi IS NULL;
--rollback ALTER TABLE accounts ALTER COLUMN token_meta_tradeapi SET NOT NULL;