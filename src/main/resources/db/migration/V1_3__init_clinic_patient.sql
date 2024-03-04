CREATE TABLE patient
(
    patient_id      SERIAL          NOT NULL,
    address_id      INT             NOT NULL,
    name            VARCHAR(50)     NOT NULL,
    surname         VARCHAR(50)     NOT NULL,
    birth_date      DATE            NOT NULL,
    email           VARCHAR(100)    NOT NULL,
    gender          VARCHAR(15)      NOT NULL,
    phone_number    VARCHAR(20)     NOT NULL,
    PRIMARY KEY(patient_id),
    CONSTRAINT fk_patient_address
        FOREIGN KEY (address_id)
            REFERENCES address(address_id)
);