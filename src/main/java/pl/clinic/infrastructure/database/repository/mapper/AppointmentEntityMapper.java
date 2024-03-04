package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentEntityMapper {
    AppointmentEntity mapToEntity(Appointment appointment);
    Appointment mapFromEntity(AppointmentEntity appointment);
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "doctor.doctorAvailabilities", ignore = true)
    DoctorAvailability mapFromEntity(DoctorAvailabilityEntity doctorAvailability);
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "doctor.doctorAvailabilities", ignore = true)
    DoctorAvailabilityEntity mapToEntity(DoctorAvailability doctorAvailability);
    @Mapping(target = "appointment", ignore = true)
    Prescription mapFromEntity(PrescriptionEntity prescriptionEntity);
    @Mapping(target = "appointment", ignore = true)
    PrescriptionEntity mapToEntity(Prescription prescriptionEntity);
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "address.patient", ignore = true)
    Patient mapFromEntity(PatientEntity patient);
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "address.patient", ignore = true)
    PatientEntity mapToEntity(Patient patient);
}
