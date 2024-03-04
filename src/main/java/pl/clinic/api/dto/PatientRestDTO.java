package pl.clinic.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientRestDTO {
    private String name;
    private String surname;
    private LocalDate birthDate;
    @Email
    private String email;
    @Size
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;
    @Pattern(regexp = "^(male|female)$", message = "Gender must be either 'male' or 'female'")
    private String gender;
    private String country;
    private String city;
    private String postalCode;
    private String address;
    private String userName;
    private String password;
}
