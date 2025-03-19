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
