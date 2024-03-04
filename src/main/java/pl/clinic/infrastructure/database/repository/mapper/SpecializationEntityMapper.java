package pl.clinic.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecializationEntityMapper {
    Specialization mapFromEntity(SpecializationEntity entity);
    SpecializationEntity mapToEntity(Specialization specialization);
}
