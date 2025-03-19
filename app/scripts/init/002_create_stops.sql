DROP TABLE IF EXISTS stops CASCADE;

CREATE TABLE stops (
    stop_id TEXT PRIMARY KEY,
    stop_code INT NOT NULL,
    stop_name TEXT NOT NULL,
    stop_lat FLOAT NOT NULL,
    stop_lon FLOAT NOT NULL,
    location_type INT,
    parent_station TEXT,
    wheelchair_boarding INT CHECK (wheelchair_boarding IN (0,1)), -- 0=non, 1=oui
    stop_desc TEXT,
    zone_id TEXT,
    stop_url TEXT
);
