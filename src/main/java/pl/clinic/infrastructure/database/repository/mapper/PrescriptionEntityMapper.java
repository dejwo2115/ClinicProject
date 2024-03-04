package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrescriptionEntityMapper {
    @Mapping(target = "appointment", ignore = true)
    PrescriptionEntity mapToEntity(Prescription prescription);
    @Mapping(target = "appointment", ignore = true)
    Prescription mapFromEntity(PrescriptionEntity prescription);
}
