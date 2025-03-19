DROP TABLE IF EXISTS agency CASCADE;

CREATE TABLE agency (
    agency_id TEXT PRIMARY KEY,
    agency_name TEXT NOT NULL,
    agency_url TEXT NOT NULL,
    agency_timezone TEXT NOT NULL,
    agency_lang TEXT,
    agency_phone TEXT,
    agency_fare_url TEXT,
    agency_email TEXT 
);

----------------------------------------------------------------------
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

---------------------------------------------------------------------
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

---------------------------------------------------------------------
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

---------------------------------------------------------------------
DROP TABLE IF EXISTS stop_times CASCADE;

CREATE TABLE stop_times (
    trip_id TEXT NOT NULL,
    arrival_time TIME,
    departure_time TIME,
    stop_id TEXT NOT NULL,
    stop_sequence INT NOT NULL,
    pickup_type TEXT,
    drop_off_type TEXT,
    stop_headsign TEXT,
    PRIMARY KEY (trip_id, stop_sequence),
    FOREIGN KEY (trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE,
    FOREIGN KEY (stop_id) REFERENCES stops(stop_id) ON DELETE CASCADE
);


---------------------------------------------------------------------
DROP TABLE IF EXISTS calendar CASCADE;

CREATE TABLE calendar (
    service_id TEXT PRIMARY KEY,
    monday INT NOT NULL CHECK (monday IN (0,1)),
    tuesday INT NOT NULL CHECK (tuesday IN (0,1)),
    wednesday INT NOT NULL CHECK (wednesday IN (0,1)),
    thursday INT NOT NULL CHECK (thursday IN (0,1)),
    friday INT NOT NULL CHECK (friday IN (0,1)),
    saturday INT NOT NULL CHECK (saturday IN (0,1)),
    sunday INT NOT NULL CHECK (sunday IN (0,1)),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);


---------------------------------------------------------------------
DROP TABLE IF EXISTS calendar_dates CASCADE;

CREATE TABLE calendar_dates (
    service_id TEXT NOT NULL,
    date DATE NOT NULL,
    exception_type INT NOT NULL CHECK (exception_type IN (1,2)), -- 1=Service ajouté, 2=Service supprimé
    PRIMARY KEY (service_id, date),
    FOREIGN KEY (service_id) REFERENCES calendar(service_id) ON DELETE CASCADE
);


---------------------------------------------------------------------
DROP TABLE IF EXISTS fare_rules CASCADE;

CREATE TABLE fare_rules (
    fare_id TEXT NOT NULL,
    route_id TEXT,
    origin_id TEXT,
    destination_id TEXT,
    PRIMARY KEY (fare_id, route_id),
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);


---------------------------------------------------------------------
DROP TABLE IF EXISTS transfers CASCADE;

CREATE TABLE transfers (
    from_stop_id TEXT NOT NULL,
    to_stop_id TEXT NOT NULL,
    transfer_type INT NOT NULL CHECK (transfer_type BETWEEN 0 AND 3), -- 0=Immédiat, 1=Min d'attente, etc.
    PRIMARY KEY (from_stop_id, to_stop_id),
    FOREIGN KEY (from_stop_id) REFERENCES stops(stop_id) ON DELETE CASCADE,
    FOREIGN KEY (to_stop_id) REFERENCES stops(stop_id) ON DELETE CASCADE
);
