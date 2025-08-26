-- 010-add-account-type-to-accounts.sql

ALTER TABLE accounts
    ADD COLUMN IF NOT EXISTS account_type VARCHAR (32);

UPDATE accounts
SET account_type = COALESCE(account_type, 'SPOT');

ALTER TABLE accounts
    ALTER COLUMN account_type SET DEFAULT 'SPOT',
ALTER
COLUMN account_type SET NOT NULL;