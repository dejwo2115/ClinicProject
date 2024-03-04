package pl.clinic.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.repository.jpa.DoctorAvailabilityJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.DoctorAvailabilityEntityMapper;
import pl.clinic.infrastructure.database.repository.mapper.DoctorEntityMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.clinic.util.EntityFixtures.*;
import static pl.clinic.util.TestClassesFixtures.buildDoctor;
import static pl.clinic.util.TestClassesFixtures.buildDoctorAvailability;

@ExtendWith(MockitoExtension.class)
class DoctorAvailabilityRepositoryTest {
    @InjectMocks
    private DoctorAvailabilityRepository doctorAvailabilityRepository;
    @Mock
    private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
    @Mock
    private DoctorAvailabilityEntityMapper doctorAvailabilityEntityMapper;
    @Mock
    private DoctorJpaRepository doctorJpaRepository;
    @Mock
    private DoctorEntityMapper doctorEntityMapper;

    @Test
    void thatCreateAvailabilityForDayWorksCorrectly() {
        // given
        Doctor doctor = buildDoctor();
        List<DoctorAvailability> doctorAvailabilityList = new ArrayList<>();
        DoctorEntity savedDoctorEntity = someDoctor();
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(savedDoctorEntity);
        when(doctorJpaRepository.saveAndFlush(savedDoctorEntity)).thenReturn(savedDoctorEntity);

        // when
        doctorAvailabilityRepository.createAvailabilityForDay(doctor, doctorAvailabilityList);

        // then
        verify(doctorEntityMapper).mapToEntity(doctor);
        verify(doctorJpaRepository).saveAndFlush(savedDoctorEntity);
        verify(doctorAvailabilityJpaRepository, never()).findByDoctorAndAvailabilityDateAndStartTime(any(), any(), any());
        verify(doctorAvailabilityJpaRepository, never()).saveAndFlush(any());
    }


    @Test
    void thatFindByIdShouldReturnValue() {
        // given
        Integer id = 123;
        DoctorAvailabilityEntity doctorAvailabilityEntity = someDoctorAvailabilityWithDoctor();
        when(doctorAvailabilityJpaRepository.findById(id)).thenReturn(Optional.of(doctorAvailabilityEntity));

        DoctorAvailability expectedDoctorAvailability = buildDoctorAvailability();
        when(doctorAvailabilityEntityMapper.mapFromEntity(doctorAvailabilityEntity)).thenReturn(expectedDoctorAvailability);

        // when
        Optional<DoctorAvailability> result = doctorAvailabilityRepository.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(expectedDoctorAvailability, result.get());
    }

    @Test
    void thatFindByIdValueNotFound() {
        // given
        Integer id = 123;
        when(doctorAvailabilityJpaRepository.findById(id)).thenReturn(Optional.empty());

        // when
        Optional<DoctorAvailability> result = doctorAvailabilityRepository.findById(id);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void thatFindByDoctorShouldReturnListWithGivenDoctorAvailabilities() {
        // given
        Doctor doctor = buildDoctor();
        DoctorEntity doctorEntity = someDoctor();
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);

        DoctorAvailabilityEntity doctorAvailabilityEntity1 = someDoctorAvailabilityWithDoctor();
        DoctorAvailabilityEntity doctorAvailabilityEntity2 = someDoctorAvailability2();
        List<DoctorAvailabilityEntity> doctorAvailabilityEntityList = List.of(doctorAvailabilityEntity1, doctorAvailabilityEntity2);
        DoctorAvailability doctorAvailability1 = buildDoctorAvailability();
        DoctorAvailability doctorAvailability2 = buildDoctorAvailability().withStartTime(LocalTime.of(9,30,0));
        //when
        when(doctorAvailabilityEntityMapper.mapFromEntity(doctorAvailabilityEntity1)).thenReturn(doctorAvailability1);
        when(doctorAvailabilityEntityMapper.mapFromEntity(doctorAvailabilityEntity2)).thenReturn(doctorAvailability2);
        when(doctorAvailabilityJpaRepository.findByDoctorOrderByAvailabilityDateAscStartTimeAsc(doctorEntity)).thenReturn(doctorAvailabilityEntityList);

        List<DoctorAvailability> result = doctorAvailabilityRepository.findByDoctor(doctor);
        //then
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(a -> a.equals(doctorAvailability1)));
        assertTrue(result.stream().anyMatch(a -> a.equals(doctorAvailability2)));

        verify(doctorEntityMapper).mapToEntity(doctor);
        verify(doctorAvailabilityJpaRepository).findByDoctorOrderByAvailabilityDateAscStartTimeAsc(doctorEntity);
        verify(doctorAvailabilityEntityMapper, times(2)).mapFromEntity(any(DoctorAvailabilityEntity.class));


    }

    @Test
    void thatFindTimesForDateShouldReturnListOfTimeForSpecificDate() {
        // given
        Doctor doctor = buildDoctor();
        LocalDate date = LocalDate.of(2024, 3, 5);
        DoctorEntity doctorEntity = someDoctor();
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);

        List<LocalTime> expectedTimes = new ArrayList<>();
        expectedTimes.add(LocalTime.of(9, 0));
        expectedTimes.add(LocalTime.of(9, 30));
        when(doctorAvailabilityJpaRepository.findStartTimesByDoctorAndDate(doctorEntity, date)).thenReturn(expectedTimes);

        // when
        List<LocalTime> result = doctorAvailabilityRepository.findTimesForDate(doctor, date);

        // then
        assertEquals(expectedTimes, result);

        verify(doctorEntityMapper).mapToEntity(doctor);
        verify(doctorAvailabilityJpaRepository).findStartTimesByDoctorAndDate(doctorEntity, date);
    }

    @Test
    void thatFindByDoctorAndDateAndTimeShouldReturnUniqueValue() {
        // given
        Doctor doctor = buildDoctor();
        LocalDate selectedDate = LocalDate.of(2024, 3, 5);
        LocalTime selectedTime = LocalTime.of(9, 0);
        DoctorEntity doctorEntity = someDoctor();
        when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);

        DoctorAvailabilityEntity availabilityEntity = new DoctorAvailabilityEntity();
        when(doctorAvailabilityJpaRepository.findByDoctorAndAvailabilityDateAndStartTime(doctorEntity, selectedDate, selectedTime))
                .thenReturn(Optional.of(availabilityEntity));

        DoctorAvailability availability = buildDoctorAvailability();
        when(doctorAvailabilityEntityMapper.mapFromEntity(availabilityEntity)).thenReturn(availability);

        // when
        Optional<DoctorAvailability> result = doctorAvailabilityRepository.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime);

        // then
        assertEquals(Optional.of(availability), result);

        verify(doctorEntityMapper).mapToEntity(doctor);
        verify(doctorAvailabilityJpaRepository).findByDoctorAndAvailabilityDateAndStartTime(doctorEntity, selectedDate, selectedTime);
        verify(doctorAvailabilityEntityMapper).mapFromEntity(availabilityEntity);
    }
}