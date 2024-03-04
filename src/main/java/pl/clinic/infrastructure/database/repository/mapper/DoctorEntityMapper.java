package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorEntityMapper {
    //    @Mapping(source = "doctorAvailabilities", target = "doctorAvailabilities", qualifiedByName = "mapAvailabilities")
    @Mapping(target = "doctorAvailabilities", ignore = true)
    Doctor mapFromEntity(DoctorEntity doctorEntity);

    //    @Mapping(source = "doctorAvailabilities", target = "doctorAvailabilities", qualifiedByName = "mapAvailabilityEntities")
    @Mapping(target = "doctorAvailabilities", ignore = true)
    DoctorEntity mapToEntity(Doctor doctorEntity);

    Specialization mapFromEntity(SpecializationEntity entity);

    SpecializationEntity mapToEntity(Specialization entity);

    @Mapping(target = "appointment", ignore = true)
    DoctorAvailability mapFromEntity(DoctorAvailabilityEntity doctorAvailability);

    @Mapping(target = "appointment", ignore = true)
    DoctorAvailabilityEntity mapToEntity(DoctorAvailability doctorAvailability);

//    @Named("mapAvailabilities")
//    default Set<DoctorAvailability> mapFromEntity(Set<DoctorAvailabilityEntity> entities) {
//        return entities.stream().map(this::mapFromEntity).collect(Collectors.toSet());
//    }
//    @Named("mapAvailabilityEntities")
//    default Set<DoctorAvailabilityEntity> mapToEntity(Set<DoctorAvailability> entities) {
//        return entities.stream().map(this::mapToEntity).collect(Collectors.toSet());
//    }
}
