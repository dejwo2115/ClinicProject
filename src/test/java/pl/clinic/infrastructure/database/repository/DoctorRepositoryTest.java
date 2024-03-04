package pl.clinic.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;
import pl.clinic.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.SpecializationJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.DoctorEntityMapper;
import pl.clinic.util.EntityFixtures;
import pl.clinic.util.TestClassesFixtures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorRepositoryTest {
    @Mock
    private DoctorJpaRepository doctorJpaRepository;
    @Mock
    private DoctorEntityMapper doctorEntityMapper;
    @Mock
    private SpecializationJpaRepository specializationJpaRepository;
    @InjectMocks
    private DoctorRepository doctorRepository;

    @Test
    void thatFindAllShouldReturnListWithAllAvailableDoctors() {
        //given
        List<DoctorEntity> doctorEntities = new ArrayList<>();
        doctorEntities.add(DoctorEntity.builder().build());
        doctorEntities.add(DoctorEntity.builder().build());
        when(doctorJpaRepository.findAll()).thenReturn(doctorEntities);
        when(doctorEntityMapper.mapFromEntity(any(DoctorEntity.class))).thenReturn(Doctor.builder().build());

        //when
        List<Doctor> result = doctorRepository.findAll();

        //then
        assertEquals(2, result.size());
    }

    @Test
    void thatFindByIdShouldReturnDoctorByHisId() {
        // given
        Integer id = 123;
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        when(doctorJpaRepository.findById(id)).thenReturn(Optional.of(doctorEntity));

        Doctor expectedDoctor = TestClassesFixtures.buildDoctor();
        when(doctorEntityMapper.mapFromEntity(doctorEntity)).thenReturn(expectedDoctor);

        // when
        Optional<Doctor> result = doctorRepository.findById(id);

        // then
        assertTrue(result.isPresent());
        assertSame(result.get(), expectedDoctor);
    }

    @Test
    void thatFindByEmailShouldReturnDoctorByEmail() {
        // given
        String email = "example@example.com";
        DoctorEntity doctorEntity = DoctorEntity.builder().build();
        Doctor doctor = Doctor.builder().build();
        when(doctorJpaRepository.findByEmail(email)).thenReturn(Optional.of(doctorEntity));
        when(doctorEntityMapper.mapFromEntity(doctorEntity)).thenReturn(doctor);

        // when
        Optional<Doctor> result = doctorRepository.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertSame(result.get(), doctor);
    }

    @Test
    void thatFindBySpecializationNameShouldReturnDoctorsWithGivenSpecializationName() {
        // given
        String specialization = "Cardiology";

        List<DoctorEntity> doctorEntities = new ArrayList<>();
        doctorEntities.add(EntityFixtures.someDoctor());
        doctorEntities.add(EntityFixtures.someDoctor());

        when(doctorJpaRepository.findBySpecializationName(specialization)).thenReturn(doctorEntities);

        when(doctorEntityMapper.mapFromEntity(any(DoctorEntity.class))).thenAnswer(invocation -> {
            DoctorEntity entity = invocation.getArgument(0);
            return Doctor.builder()
                    .name(entity.getName())
                    .surname(entity.getSurname())
                    .specializations(entity.getSpecializations().stream()
                            .map(x -> doctorEntityMapper.mapFromEntity(x))
                            .collect(Collectors.toSet()))
                    .build();
        });

        // Act
        List<Doctor> result = doctorRepository.findBySpecializationName(specialization);

        // Assert
        assertEquals(result.size(), 2);
    }

    @Test
    void thatSaveWorksCorrectly() {
        // given
        Doctor doctor = TestClassesFixtures.buildDoctor();
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);


        // when
        doctorRepository.save(doctor);

        // then
        verify(doctorJpaRepository).saveAndFlush(doctorEntity);
    }

    @Test
    void thatSaveSpecToDoctorShouldAddNewSpecialization() {
        // given
        Doctor doctor = TestClassesFixtures.buildDoctor();
        Specialization specialization = Specialization.builder().specializationId(4).name("Cardiology").build();
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        SpecializationEntity specializationEntity = SpecializationEntity.builder().specializationId(4).name("Cardiology").build();
        doctorEntity.setSpecializations(new HashSet<>());
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);

        when(doctorEntityMapper.mapToEntity(specialization)).thenReturn(specializationEntity);

        // when
        doctorRepository.saveSpecToDoctor(doctor, specialization);

        // then
        verify(specializationJpaRepository).saveAndFlush(specializationEntity);
        verify(doctorJpaRepository).saveAndFlush(doctorEntity);
    }
}