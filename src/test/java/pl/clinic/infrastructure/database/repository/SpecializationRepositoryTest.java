package pl.clinic.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.domain.Specialization;
import pl.clinic.domain.exception.ProcessingException;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;
import pl.clinic.infrastructure.database.repository.jpa.SpecializationJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.SpecializationEntityMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecializationRepositoryTest {
    @InjectMocks
    private SpecializationRepository specializationRepository;
    @Mock
    private SpecializationJpaRepository specializationJpaRepository;
    @Mock
    private SpecializationEntityMapper specializationEntityMapper;

    @Test
    void findAll() {
        // Given
        List<SpecializationEntity> entityList = List.of(
                new SpecializationEntity(1, "Specialization 1"),
                new SpecializationEntity(2, "Specialization 2")
        );

        List<Specialization> expectedSpecializations = entityList.stream()
                .map(specializationEntityMapper::mapFromEntity)
                .toList();

        when(specializationJpaRepository.findAll()).thenReturn(entityList);
        when(specializationEntityMapper.mapFromEntity(entityList.get(0))).thenReturn(expectedSpecializations.get(0));
        when(specializationEntityMapper.mapFromEntity(entityList.get(1))).thenReturn(expectedSpecializations.get(1));

        // When
        List<Specialization> actualSpecializations = specializationRepository.findAll();

        // Then
        assertEquals(expectedSpecializations.size(), actualSpecializations.size());
        for (int i = 0; i < expectedSpecializations.size(); i++) {
            assertEquals(expectedSpecializations.get(i), actualSpecializations.get(i));
        }
    }


    @Test
    void findBySpecializationName() {
        // Given
        String specializationName = "Specialization 1";
        SpecializationEntity entity = new SpecializationEntity(1, specializationName);
        Specialization expectedSpecialization = Specialization.builder().specializationId(1).name(specializationName).build();

        when(specializationJpaRepository.findByName(specializationName)).thenReturn(Optional.of(entity));
        when(specializationEntityMapper.mapFromEntity(entity)).thenReturn(expectedSpecialization);

        // When
        Optional<Specialization> actualSpecializationOptional = specializationRepository.findBySpecializationName(specializationName);

        // Then
        assertEquals(Optional.of(expectedSpecialization), actualSpecializationOptional);
    }

    @Test
    void thatFindBySpecializationNameReturnOptionalEmpty() {
        // Given
        String specializationName = "Non-existing Specialization";

        when(specializationJpaRepository.findByName(specializationName)).thenReturn(Optional.empty());

        // When
        Optional<Specialization> actualSpecializationOptional = specializationRepository.findBySpecializationName(specializationName);

        // Then
        assertEquals(Optional.empty(), actualSpecializationOptional);

    }
    @Test
    void testSave_NewSpecialization() {
        // Given
        String specializationName = "New Specialization";

        when(specializationJpaRepository.findByName(specializationName)).thenReturn(null);
        // When
        specializationRepository.save(specializationName);

        // Then
        Mockito.verify(specializationJpaRepository).saveAndFlush(any());
    }

    @Test
    void testSave_SpecializationExists() {
        // Given
        String specializationName = "Existing Specialization";
        when(specializationJpaRepository.findByName(specializationName))
                .thenReturn(Optional.ofNullable(SpecializationEntity.builder().build()));

        // When/Then
        Throwable exception = assertThrows(ProcessingException.class, () -> specializationRepository.save(specializationName));

        // then
        assertEquals("Specialization [%s], is already in database.".formatted(specializationName), exception.getMessage());
        assertThrows(ProcessingException.class, () -> specializationRepository.save(specializationName));
        Mockito.verify(specializationJpaRepository, never()).saveAndFlush(any());
    }
}