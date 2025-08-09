--liquibase formatted sql

--changeset volodymyr.mishchuk:003-fix-token-column

ALTER TABLE accounts
    RENAME COLUMN "token_MetaTradeAPI" TO "token_meta_tradeapi";
