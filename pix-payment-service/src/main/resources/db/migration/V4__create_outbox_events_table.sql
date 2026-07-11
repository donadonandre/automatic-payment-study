CREATE TABLE outbox_events (
                               id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               aggregate_type  VARCHAR(50)     NOT NULL,
                               aggregate_id    UUID            NOT NULL,
                               event_type      VARCHAR(100)    NOT NULL,
                               payload         JSONB           NOT NULL,
                               kafka_key       VARCHAR(100)    NOT NULL,
                               published       BOOLEAN         NOT NULL DEFAULT false,
                               created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
                               published_at    TIMESTAMPTZ
);

CREATE INDEX idx_outbox_events_unpublished ON outbox_events (created_at) WHERE published = false;