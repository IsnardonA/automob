DROP TABLE IF EXISTS routes CASCADE;

CREATE TABLE routes (
    route_id TEXT PRIMARY KEY,
    agency_id TEXT,
    route_short_name TEXT NOT NULL,
    route_long_name TEXT NOT NULL,
    route_type INT NOT NULL, -- 0=Tram, 3=Bus, etc.
    route_color TEXT,
    route_text_color TEXT,
    route_url TEXT,
    FOREIGN KEY (agency_id) REFERENCES agency(agency_id) ON DELETE CASCADE
);
