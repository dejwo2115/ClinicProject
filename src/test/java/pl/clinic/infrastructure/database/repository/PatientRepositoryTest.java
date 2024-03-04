package pl.clinic.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.domain.Patient;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.PatientEntityMapper;
import pl.clinic.util.TestClassesFixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryTest {
    @Mock
    private PatientJpaRepository patientJpaRepository;
    @Mock
    private PatientEntityMapper patientEntityMapper;
    @InjectMocks
    private PatientRepository patientRepository;

    @Test
    void savePatient() {
        // given
        Patient patient = TestClassesFixtures.buildPatient();
        PatientEntity patientEntity = new PatientEntity();
        when(patientEntityMapper.mapToEntity(patient)).thenReturn(patientEntity);

        // when
        patientRepository.savePatient(patient);
        // then
        Mockito.verify(patientJpaRepository).saveAndFlush(patientEntity);
    }


    @Test
    void findByEmail() {
        // given
        String email = "example@example.com";
        PatientEntity patientEntity = PatientEntity.builder().build();
        Patient patient = Patient.builder().build();
        when(patientJpaRepository.findByEmail(email)).thenReturn(Optional.of(patientEntity));
        when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        // when
        Optional<Patient> result = patientRepository.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertSame(result.get(), patient);
    }


    @Test
    void findAll() {
        //given
        List<PatientEntity> patientEntities = new ArrayList<>();
        patientEntities.add(new PatientEntity());
        patientEntities.add(new PatientEntity());
        when(patientJpaRepository.findAll()).thenReturn(patientEntities);
        when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class))).thenReturn(Patient.builder().build());

        //when
        List<Patient> result = patientRepository.findAll();

        //then
        assertEquals(2, result.size());
    }
}