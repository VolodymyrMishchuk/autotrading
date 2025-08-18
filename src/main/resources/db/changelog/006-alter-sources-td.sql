--liquibase formatted sql

--changeset volodymyr.mishchuk:006-alter-sources-td

ALTER TABLE sources
    ADD COLUMN td_session_id UUID,
    ADD COLUMN chat_id BIGINT,
    ADD COLUMN settings JSONB;

ALTER TABLE sources
    ADD CONSTRAINT fk_sources_td_session
        FOREIGN KEY (td_session_id) REFERENCES td_session(id)
            ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_sources_td_session ON sources(td_session_id);
CREATE INDEX IF NOT EXISTS idx_sources_chat_id   ON sources(chat_id);

--rollback DROP INDEX IF EXISTS idx_sources_chat_id;
--rollback DROP INDEX IF EXISTS idx_sources_td_session;
--rollback ALTER TABLE sources DROP CONSTRAINT IF EXISTS fk_sources_td_session;
--rollback ALTER TABLE sources DROP COLUMN IF EXISTS settings;
--rollback ALTER TABLE sources DROP COLUMN IF EXISTS chat_id;
--rollback ALTER TABLE sources DROP COLUMN IF EXISTS td_session_id;
