package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;

@With
@Value
@Builder
@EqualsAndHashCode(of = "prescriptionId")
@ToString(of = {"prescriptionId", "prescriptionCode", "issueDate", "prescribedMedications", "note"})
public class Prescription {
    Integer prescriptionId;
    String prescriptionCode;
    LocalDate issueDate;
    LocalDate expirationDate;
    String prescribedMedications;
    String note;
    Appointment appointment;
}
