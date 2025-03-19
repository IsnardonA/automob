DROP TABLE IF EXISTS trips CASCADE;

CREATE TABLE trips (
    trip_id TEXT PRIMARY KEY,
    route_id TEXT NOT NULL,
    service_id TEXT NOT NULL,
    trip_headsign TEXT,
    direction_id INT CHECK (direction_id IN (0,1)), -- 0=Aller, 1=Retour
    block_id TEXT,
    wheelchair_accessible TEXT, -- 0=non, 1=oui
    bikes_allowed TEXT, -- 0=non, 1=oui
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);
