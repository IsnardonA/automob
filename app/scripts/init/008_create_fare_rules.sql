DROP TABLE IF EXISTS fare_rules CASCADE;

CREATE TABLE fare_rules (
    fare_id TEXT NOT NULL,
    route_id TEXT,
    origin_id TEXT,
    destination_id TEXT,
    PRIMARY KEY (fare_id, route_id),
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);
