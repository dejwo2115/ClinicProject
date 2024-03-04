CREATE TABLE doctor_specialization
(
    doctor_id                   INT          NOT NULL,
    specialization_id           INT          NOT NULL,
     CONSTRAINT fk_doctor_specialization_doctor
            FOREIGN KEY (doctor_id)
                REFERENCES doctor(doctor_id),
     CONSTRAINT fk_doctor_specialization_specialization
            FOREIGN KEY (specialization_id)
                REFERENCES specialization(specialization_id)
);
