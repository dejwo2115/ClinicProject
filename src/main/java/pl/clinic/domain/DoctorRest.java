package pl.clinic.domain;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"name", "surname", "birthDate", "gender", "email"})
public class DoctorRest {
    String name;
    String surname;
    LocalDate birthDate;
    @Email
    String email;
    String gender;
    String userName;
    String password;
}
