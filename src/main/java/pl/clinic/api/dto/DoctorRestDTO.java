package pl.clinic.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRestDTO {
    private String name;
    private String surname;
    private LocalDate birthDate;
    @Email
    private String email;
    @Pattern(regexp = "^(male|female)$", message = "Gender must be either 'male' or 'female'")
    private String gender;
    private String userName;
    private String password;
}
