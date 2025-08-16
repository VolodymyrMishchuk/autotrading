--liquibase formatted sql

--changeset volodymyr.mishchuk:005-create-td-session

CREATE TABLE td_session
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(64)  NOT NULL,
    api_id     INT          NOT NULL,
    api_hash   VARCHAR(128) NOT NULL,
    db_dir     TEXT         NOT NULL,
    status     VARCHAR(16)  NOT NULL DEFAULT 'INACTIVE',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    CONSTRAINT td_session_status_chk CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT td_session_db_dir_uniq UNIQUE (db_dir)
);

CREATE INDEX IF NOT EXISTS idx_td_session_status ON td_session(status);

--rollback DROP INDEX IF EXISTS idx_td_session_status;
--rollback DROP TABLE IF EXISTS td_session;
