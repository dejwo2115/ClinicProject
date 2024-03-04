package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "appointmentId")
@ToString(of = {"appointmentId", "reservedDate", "reservedStartTime", "isFinished", "canceled", "note"})
public class Appointment {
    Integer appointmentId;
    LocalDate reservedDate;
    LocalTime reservedStartTime;
    Boolean isFinished;
    Boolean canceled;
    String note;
    DoctorAvailability doctorAvailability;
    Patient patient;
    Prescription prescription;
}
