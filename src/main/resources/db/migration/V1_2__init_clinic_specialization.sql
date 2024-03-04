CREATE TABLE specialization
(
    specialization_id       SERIAL          NOT NULL,
    name                    VARCHAR(50)     NOT NULL,
    PRIMARY KEY(specialization_id),
    UNIQUE(name)
);