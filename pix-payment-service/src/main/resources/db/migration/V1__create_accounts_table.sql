CREATE TABLE accounts (
                          id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          holder_name     VARCHAR(150)    NOT NULL,
                          document_number VARCHAR(14)     NOT NULL UNIQUE,
                          balance_cents   BIGINT          NOT NULL DEFAULT 0,
                          currency        CHAR(3)         NOT NULL DEFAULT 'BRL',
                          version         BIGINT          NOT NULL DEFAULT 0,
                          created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                          updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                          CONSTRAINT chk_balance_non_negative CHECK (balance_cents >= 0)
);

CREATE INDEX idx_accounts_document_number ON accounts (document_number);