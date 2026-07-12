CREATE TYPE pix_key_type AS ENUM ('CPF', 'CNPJ', 'EMAIL', 'PHONE', 'RANDOM');

CREATE TABLE pix_keys (
                          id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          account_id  UUID            NOT NULL REFERENCES accounts (id),
                          key_value   VARCHAR(77)     NOT NULL UNIQUE,
                          key_type    pix_key_type    NOT NULL,
                          active      BOOLEAN         NOT NULL DEFAULT true,
                          created_at  TIMESTAMPTZ     NOT NULL DEFAULT now()
);

CREATE INDEX idx_pix_keys_account_id ON pix_keys (account_id);