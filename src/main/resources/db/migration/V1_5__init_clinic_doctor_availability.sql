CREATE TABLE doctor_availability
(
    doctor_availability_id      SERIAL  NOT NULL,
    doctor_id                   INT     NOT NULL,
    availability_date           DATE    NOT NULL,
    start_time                  TIME    NOT NULL,
    reserved                    BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(doctor_availability_id),
    CONSTRAINT fk_doctor_availability_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id)
);