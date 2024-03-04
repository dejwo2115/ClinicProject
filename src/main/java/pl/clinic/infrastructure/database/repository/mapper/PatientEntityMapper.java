package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Address;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.database.entity.AddressEntity;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientEntityMapper {
//    @Mapping(source = "appointments",target = "appointments", qualifiedByName = "mapAppointmentsEntities")
    PatientEntity mapToEntity(Patient patient);
//    @Mapping(source = "appointments",target = "appointments", qualifiedByName = "mapAppointments")
    Patient mapFromEntity(PatientEntity patient);
    @Mapping(target = "patient", ignore = true)
    Address mapFromEntity(AddressEntity entity);
    @Mapping(target = "patient", ignore = true)
    AddressEntity mapToEntity(Address entity);
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctorAvailability", ignore = true)
    @Mapping(target = "prescription.appointment", ignore = true)
    Appointment mapFromEntity(AppointmentEntity appointment);
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctorAvailability", ignore = true)
    @Mapping(target = "prescription.appointment", ignore = true)
    AppointmentEntity mapToEntity(Appointment appointment);
    @Mapping(target = "appointment", ignore = true)
    Prescription mapFromEntity(PrescriptionEntity prescriptionEntity);
    @Mapping(target = "appointment", ignore = true)
    PrescriptionEntity mapToEntity(Prescription prescription);
}
