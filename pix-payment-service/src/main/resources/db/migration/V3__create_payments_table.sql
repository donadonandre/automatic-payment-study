CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'REVERSED');

CREATE TABLE payments (
                          id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          source_account_id   UUID            NOT NULL REFERENCES accounts (id),
                          target_pix_key       VARCHAR(77)     NOT NULL,
                          amount_cents        BIGINT          NOT NULL,
                          currency            CHAR(3)         NOT NULL DEFAULT 'BRL',
                          status               payment_status  NOT NULL DEFAULT 'PENDING',
                          failure_reason       VARCHAR(255),
                          idempotency_key      VARCHAR(100)    NOT NULL UNIQUE,
                          version              BIGINT          NOT NULL DEFAULT 0,
                          created_at           TIMESTAMPTZ     NOT NULL DEFAULT now(),
                          updated_at           TIMESTAMPTZ     NOT NULL DEFAULT now(),
                          CONSTRAINT chk_amount_positive CHECK (amount_cents > 0)
);

CREATE INDEX idx_payments_source_account_id ON payments (source_account_id);
CREATE INDEX idx_payments_status ON payments (status);