package pl.clinic.infrastructure.database.repository.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;

import static org.junit.jupiter.api.Assertions.assertNull;

class SpecializationEntityMapperTest {
    private final SpecializationEntityMapper mapper = new SpecializationEntityMapperImpl();

    @Test
    void mapFromEntityShouldReturnSpecialization() {
        //given
        SpecializationEntity specializationEntity = SpecializationEntity.builder().specializationId(1).name("someSpec").build();

        //when
        Specialization specialization = mapper.mapFromEntity(specializationEntity);
        //then
        Assertions.assertThat(specializationEntity.getName()).isEqualTo(specialization.getName());
        Assertions.assertThat(specializationEntity.getSpecializationId()).isEqualTo(specialization.getSpecializationId());

    }

    @Test
    void mapToEntityShouldReturnSpecializationEntity() {
        //given
        Specialization specialization = Specialization.builder().specializationId(1).name("someSpec").build();
        //when
        SpecializationEntity specializationEntity = mapper.mapToEntity(specialization);
        //then
        Assertions.assertThat(specialization.getName()).isEqualTo(specializationEntity.getName());
        Assertions.assertThat(specialization.getSpecializationId()).isEqualTo(specializationEntity.getSpecializationId());

    }
    @Test
    void mapFromEntity_withNullEntity_shouldReturnNull() {
        // When
        Specialization specialization = mapper.mapFromEntity(null);

        // Then
        assertNull(specialization);
    }
    @Test
    void mapToEntity_withNullSpecialization_shouldReturnNull() {
        // When
        SpecializationEntity entity = mapper.mapToEntity(null);

        // Then
        assertNull(entity);
    }
}