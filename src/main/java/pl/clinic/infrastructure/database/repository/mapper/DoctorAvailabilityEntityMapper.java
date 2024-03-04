package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorAvailabilityEntityMapper {
    @Mapping(target = "appointment", ignore = true)
    DoctorAvailability mapFromEntity(DoctorAvailabilityEntity doctorAvailability);
    @Mapping(target = "appointment", ignore = true)
    DoctorAvailabilityEntity mapToEntity(DoctorAvailability doctorAvailability);
    @Mapping(target = "doctorAvailabilities", ignore = true)
    Doctor mapFromEntity(DoctorEntity doctorEntity);
    @Mapping(target = "doctorAvailabilities", ignore = true)
    DoctorEntity mapToEntity(Doctor doctorEntity);
}
