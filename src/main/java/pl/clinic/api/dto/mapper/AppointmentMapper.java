package pl.clinic.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.PrescriptionDTO;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Prescription;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentDTO map(Appointment appointment);
    @Mapping(target = "doctorAvailability", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment map(AppointmentDTO appointment);
    List<AppointmentDTO> map(List<Appointment> appointment);
    PrescriptionDTO map(Prescription prescription);
    @Mapping(target = "appointment", ignore = true)
    Prescription map(PrescriptionDTO prescription);

}
