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
public class DoctorAvailabilityDTO {
    private Integer doctorAvailabilityId;
    private LocalDate availabilityDate;
    private LocalTime startTime;
    private Boolean reserved;
}
