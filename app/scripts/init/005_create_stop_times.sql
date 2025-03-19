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
