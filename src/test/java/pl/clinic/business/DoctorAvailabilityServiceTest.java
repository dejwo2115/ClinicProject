package pl.clinic.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.business.dao.DoctorAvailabilityDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.exception.NotFoundException;
import pl.clinic.domain.exception.ProcessingException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.buildDoctor;
import static pl.clinic.util.TestClassesFixtures.buildDoctorAvailability;

@ExtendWith(MockitoExtension.class)
class DoctorAvailabilityServiceTest {
    @InjectMocks
    private DoctorAvailabilityService doctorAvailabilityService;
    @Mock
    private DoctorAvailabilityDAO doctorAvailabilityDAO;

    @Test
    void thatCreateAvailabilityForDayWorksCorrectly() {
        //given
        Doctor doctor = buildDoctor();
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(9, 0, 0);
        Integer amountOfVisits = 5;

        //when
        doctorAvailabilityService.createAvailabilityForDay(date, time, amountOfVisits, doctor);

        //then
        verify(doctorAvailabilityDAO).createAvailabilityForDay(eq(doctor), anyList());

    }

    @Test
    void thatCreateAvailabilityForDayShouldThrowIfTryingToInsertDateFromPast() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        LocalTime time = LocalTime.of(9, 0);
        Integer amountOfVisits = 3;
        Doctor doctor = buildDoctor();

        // when, then
        assertThrows(ProcessingException.class, () ->
                doctorAvailabilityService.createAvailabilityForDay(date, time, amountOfVisits, doctor));
        verifyNoInteractions(doctorAvailabilityDAO);
    }

    @Test
    void createAvailabilityForDayShouldCreateCorrectNumberOfAvailabilities() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(9, 0);
        Integer amountOfVisits = 5;
        Doctor doctor = buildDoctor();

        // when
        doctorAvailabilityService.createAvailabilityForDay(date, time, amountOfVisits, doctor);

        // then
        verify(doctorAvailabilityDAO).createAvailabilityForDay(eq(doctor), anyList());
        verify(doctorAvailabilityDAO, times(1)).createAvailabilityForDay(any(), anyList());
    }

    @Test
    void thatFindByIdShouldReturnOptionalDoctorAvailability() {
        // given
        Integer id = 1;
        Doctor doctor = buildDoctor();
        DoctorAvailability expectedAvailability =
                buildDoctorAvailability(doctor).withDoctorAvailabilityId(id);
        when(doctorAvailabilityDAO.findById(id)).thenReturn(Optional.of(expectedAvailability));

        // when
        DoctorAvailability actualAvailability = doctorAvailabilityService.findById(id);

        // then
        assertEquals(expectedAvailability, actualAvailability);
    }

    @Test
    void thatFindByIdShouldThrowIfNotFound() {
        // given
        Integer id = 1;
        when(doctorAvailabilityDAO.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> doctorAvailabilityService.findById(id));
    }

    @Test
    void thatFindByDoctorShouldReturnListWithAvailabilitiesForGivenDoctor() {
        // given
        Doctor doctor = buildDoctor();
        List<DoctorAvailability> expectedList = new ArrayList<>();
        expectedList.add(buildDoctorAvailability(doctor));
        expectedList.add(buildDoctorAvailability(doctor)
                .withDoctorAvailabilityId(1)
                .withStartTime(LocalTime.now().plusMinutes(30)));
        when(doctorAvailabilityDAO.findByDoctor(doctor)).thenReturn(expectedList);

        // when
        List<DoctorAvailability> actualList = doctorAvailabilityService.findByDoctor(doctor);

        // then
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }


    @Test
    void thatFindTimesForDateWorksCorrectly() {
        // given
        Doctor doctor = buildDoctor();
        LocalDate date = LocalDate.now();
        List<LocalTime> expectedTimes = new ArrayList<>();
        expectedTimes.add(LocalTime.of(9, 0));
        expectedTimes.add(LocalTime.of(9, 30));
        expectedTimes.add(LocalTime.of(10, 0));
        when(doctorAvailabilityDAO.findTimesForDate(doctor, date)).thenReturn(expectedTimes);

        // when
        List<LocalTime> actualTimes = doctorAvailabilityService.findTimesForDate(doctor, date);

        // then
        assertEquals(expectedTimes.size(), actualTimes.size());
        assertEquals(expectedTimes, actualTimes);
    }

    @Test
    void thatFindByDoctorAndDateAndTimeWillFindSpecificAvailability() {
        // given
        Doctor doctor = buildDoctor();
        LocalDate selectedDate = LocalDate.of(2024, 3, 4);
        LocalTime selectedTime = LocalTime.of(9, 30);
        DoctorAvailability expectedAvailability =
                buildDoctorAvailability(doctor)
                        .withAvailabilityDate(selectedDate)
                        .withStartTime(selectedTime);
        when(doctorAvailabilityDAO.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime))
                .thenReturn(Optional.of(expectedAvailability));

        // when
        DoctorAvailability actualAvailability = doctorAvailabilityService.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime);

        // then
        assertEquals(expectedAvailability, actualAvailability);
    }

    @Test
    void thatFindByDoctorAndDateAndTimeShouldThrowIfSpecificAvailabilityDoesNotExist() {
        // given
        Doctor doctor = buildDoctor();
        LocalDate selectedDate = LocalDate.of(2024, 3, 4);
        LocalTime selectedTime = LocalTime.of(9, 0);
        when(doctorAvailabilityDAO.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime))
                .thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () ->
                doctorAvailabilityService.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime));
    }

}