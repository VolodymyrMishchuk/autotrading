--liquibase formatted sql

--changeset volodymyr.mishchuk:2

DROP TABLE IF EXISTS Transactions CASCADE;
DROP TABLE IF EXISTS Accounts CASCADE;
DROP TABLE IF EXISTS Sources CASCADE;
DROP TABLE IF EXISTS Persons CASCADE;

CREATE TABLE Persons (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(16) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(15) NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ
);

CREATE TABLE Accounts (
    id UUID PRIMARY KEY,
    number BIGINT NOT NULL UNIQUE,
    status VARCHAR(10) NOT NULL,
    balance MONEY NOT NULL DEFAULT 0 CHECK (balance >= 0),
    currency CHAR(3) NOT NULL,
    token_MetaTradeAPI TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    person_id UUID NOT NULL REFERENCES Persons(id)
);

CREATE TABLE Sources (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    token TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ
);

CREATE TABLE Transactions (
    id UUID PRIMARY KEY,
    amount MONEY NOT NULL CHECK (amount > 0),
    direction VARCHAR(15) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    account_id UUID NOT NULL REFERENCES Accounts(id),
    source_id UUID NOT NULL REFERENCES Sources(id)
);