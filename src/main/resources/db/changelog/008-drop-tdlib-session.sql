--liquibase formatted sql

--changeset autotrade:008-drop-tdlib-session

ALTER TABLE IF EXISTS sources
    DROP CONSTRAINT IF EXISTS fk_sources_td_session;

ALTER TABLE IF EXISTS sources
    DROP COLUMN IF EXISTS td_session_id;

DROP TABLE IF EXISTS td_session;

--rollback
CREATE TABLE td_session (
                            id UUID PRIMARY KEY,
                            name VARCHAR(64) NOT NULL,
                            api_id INT NOT NULL,
                            api_hash VARCHAR(128) NOT NULL,
                            db_dir VARCHAR(255) NOT NULL,
                            status VARCHAR(16) NOT NULL,
                            created_at TIMESTAMPTZ DEFAULT now(),
                            updated_at TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX idx_td_session_status ON td_session(status);

ALTER TABLE sources
    ADD COLUMN td_session_id UUID;

ALTER TABLE sources
    ADD CONSTRAINT fk_sources_td_session
        FOREIGN KEY (td_session_id) REFERENCES td_session(id)
            ON DELETE SET NULL;
