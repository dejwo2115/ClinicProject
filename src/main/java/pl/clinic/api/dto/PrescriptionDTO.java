package pl.clinic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Integer prescriptionId;
    private String prescriptionCode;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private String prescribedMedications;
    private String note;
}
