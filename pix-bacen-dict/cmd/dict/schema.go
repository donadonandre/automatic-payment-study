package main

const schemaSQL = `
CREATE TABLE IF NOT EXISTS pix_keys (
    key_value        TEXT PRIMARY KEY,
    key_type         TEXT NOT NULL CHECK (key_type IN ('CPF','CNPJ','EMAIL','PHONE','RANDOM')),
    participant_ispb TEXT NOT NULL,
    account_ref      TEXT NOT NULL,
    created_at       TEXT NOT NULL DEFAULT (datetime('now'))
);
`
