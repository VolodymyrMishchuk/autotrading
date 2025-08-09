--liquibase formatted sql

--changeset volodymyr.mishchuk:004-drop-token-from-accounts

ALTER TABLE accounts DROP COLUMN IF EXISTS token_meta_tradeapi;