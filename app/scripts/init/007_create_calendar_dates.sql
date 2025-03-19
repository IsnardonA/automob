DROP TABLE IF EXISTS calendar_dates CASCADE;

CREATE TABLE calendar_dates (
    service_id TEXT NOT NULL,
    date DATE NOT NULL,
    exception_type INT NOT NULL CHECK (exception_type IN (1,2)), -- 1=Service ajouté, 2=Service supprimé
    PRIMARY KEY (service_id, date),
    FOREIGN KEY (service_id) REFERENCES calendar(service_id) ON DELETE CASCADE
);
