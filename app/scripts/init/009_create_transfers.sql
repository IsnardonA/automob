DROP TABLE IF EXISTS transfers CASCADE;

CREATE TABLE transfers (
    from_stop_id TEXT NOT NULL,
    to_stop_id TEXT NOT NULL,
    transfer_type INT NOT NULL CHECK (transfer_type BETWEEN 0 AND 3), -- 0=Immédiat, 1=Min d'attente, etc.
    PRIMARY KEY (from_stop_id, to_stop_id),
    FOREIGN KEY (from_stop_id) REFERENCES stops(stop_id) ON DELETE CASCADE,
    FOREIGN KEY (to_stop_id) REFERENCES stops(stop_id) ON DELETE CASCADE
);
