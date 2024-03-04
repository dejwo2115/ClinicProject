package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "doctorAvailabilityId")
@ToString(of = {"doctorAvailabilityId", "availabilityDate", "startTime", "reserved"})
public class DoctorAvailability {
    Integer doctorAvailabilityId;
    LocalDate availabilityDate;
    LocalTime startTime;
    Boolean reserved;
    Doctor doctor;
    Appointment appointment;
}
