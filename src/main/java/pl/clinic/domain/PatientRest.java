package pl.clinic.domain;

import lombok.*;

import java.time.LocalDate;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"name", "surname", "birthDate", "gender", "email"})
public class PatientRest {
    String name;
    String surname;
    LocalDate birthDate;
    String email;
    String phone;
    String gender;
    String country;
    String city;
    String postalCode;
    String address;
    String userName;
    String password;
}
