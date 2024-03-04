CREATE TABLE prescription
(
    prescription_id         SERIAL              NOT NULL,
    prescription_code       VARCHAR(300)         NOT NULL,
    issue_date              DATE                NOT NULL,
    expiration_date         DATE                NOT NULL,
    prescribed_medications  VARCHAR(1000)       NOT NULL,
    note                    VARCHAR(1000)       NOT NULL,
    PRIMARY KEY(prescription_id),
    UNIQUE(prescription_code)
);