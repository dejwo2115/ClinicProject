CREATE TABLE appointment
(
    appointment_id          SERIAL                      NOT NULL,
    doctor_availability_id  INT                         NOT NULL,
    patient_id              INT                         NOT NULL,
    prescription_id         INT,
    reserved_date           DATE                        NOT NULL,
    reserved_start_time     TIME                        NOT NULL,
    is_finished             BOOLEAN       DEFAULT false NOT NULL,
    canceled                BOOLEAN       DEFAULT false NOT NULL,
    note                    TEXT,
    PRIMARY KEY(appointment_id),
    CONSTRAINT fk_appointment_doctor_availability
        FOREIGN KEY (doctor_availability_id)
            REFERENCES doctor_availability(doctor_availability_id),
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_appointment_prescription
        FOREIGN KEY (prescription_id)
            REFERENCES prescription(prescription_id)
);