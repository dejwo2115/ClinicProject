package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "patientId")
@ToString(of = {"patientId", "name", "surname", "birthDate", "email", "phone", "gender"})
public class Patient {
    Integer patientId;
    String name;
    String surname;
    LocalDate birthDate;
    String email;
    String phone;
    String gender;
    Address address;
    Integer userId;
    Set<Appointment> appointments;
}
