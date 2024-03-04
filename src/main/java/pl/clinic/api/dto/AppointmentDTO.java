package pl.clinic.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Integer appointmentId;
    private LocalDate reservedDate;
    private LocalTime reservedStartTime;
    private Boolean isFinished;
    private Boolean canceled;
    private String note;
}
