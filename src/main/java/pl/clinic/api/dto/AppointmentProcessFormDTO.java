package pl.clinic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentProcessFormDTO {
    private String appointmentNote;
    private String prescribedMedications;
    private String prescriptionNote;
}
