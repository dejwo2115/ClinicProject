package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"doctorId", "name", "surname", "birthDate", "gender", "email"})
public class Doctor {
    Integer doctorId;
    String name;
    String surname;
    LocalDate birthDate;
    String email;
    String gender;
    Integer userId;
    Set<Specialization> specializations;
    Set<DoctorAvailability> doctorAvailabilities;
}
