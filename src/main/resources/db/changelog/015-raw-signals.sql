--liquibase formatted sql
--changeset autotrade:015-raw-signals

CREATE TABLE IF NOT EXISTS raw_signals (
                                           id BIGSERIAL PRIMARY KEY,
                                           raw_message TEXT NOT NULL,
                                           received_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);
