CREATE TABLE doctor
(
    doctor_id           SERIAL          NOT NULL,
    name                VARCHAR(50)     NOT NULL,
    surname             VARCHAR(50)     NOT NULL,
    gender              VARCHAR(15)     NOT NULL,
    email               VARCHAR(100)    NOT NULL,
    birth_date          DATE            NOT NULL,
    specialization_id   INT,
    PRIMARY KEY(doctor_id),
    CONSTRAINT fk_doctor_specialization
        FOREIGN KEY(specialization_id)
            REFERENCES specialization(specialization_id)
);