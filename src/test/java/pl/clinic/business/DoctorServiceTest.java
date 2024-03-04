package pl.clinic.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.business.dao.DoctorDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;
import pl.clinic.domain.exception.NotFoundException;
import pl.clinic.domain.exception.ProcessingException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.clinic.util.TestClassesFixtures.buildDoctor;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {
    @Mock
    private DoctorDAO doctorDAO;
    @InjectMocks
    private DoctorService doctorService;

    @Test
    void thatFindAllShouldReturnListOfDoctors() {
        // given
        List<Doctor> doctors = List.of(buildDoctor(), buildDoctor().withDoctorId(2));
        when(doctorDAO.findAll()).thenReturn(doctors);

        // when
        List<Doctor> result = doctorService.findAll();

        // then
        assertEquals(2, result.size());
        assertEquals(doctors, result);
        assertNotEquals(List.of(), result);
    }

    @Test
    void findByIdWithValidIdShouldReturnOptionalDoctor() {
        // given
        Integer id = 1;
        Doctor doctor = buildDoctor().withDoctorId(1);
        when(doctorDAO.findById(id)).thenReturn(Optional.of(doctor));

        // when
        Optional<Doctor> result = doctorService.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(doctor, result.get());
    }

    @Test
    void thatFindByIdShouldThrowIfNotFound() {
        // given
        Integer incorrectId = 99;
        when(doctorDAO.findById(incorrectId)).thenReturn(Optional.empty());

        //when, then
        Throwable notFoundException = assertThrows(NotFoundException.class, () -> doctorService.findById(incorrectId));
        assertEquals("Could not find doctor with id: [%s]".formatted(incorrectId), notFoundException.getMessage());
    }
    @Test
    void thatFindByEmailShouldThrowIfNotFound() {
        // given
        String badEmail = "badEmail";
        when(doctorDAO.findByEmail(badEmail)).thenReturn(Optional.empty());


        //when, then
        Throwable notFoundException = assertThrows(NotFoundException.class, () -> doctorService.findByEmail(badEmail));
        assertEquals("Could not find doctor with email: [%s]".formatted(badEmail), notFoundException.getMessage());
    }

    @Test
    void findByEmailShouldReturnOptionalDoctor() {
        // given
        String email = "doctor@example.com";
        Doctor doctor = buildDoctor().withEmail(email);
        when(doctorDAO.findByEmail(email)).thenReturn(Optional.of(doctor));

        // when
        Doctor result = doctorService.findByEmail(email);

        // then
        assertEquals(doctor, result);
    }

    @Test
    void findDoctorsBySpecializationNameShouldReturnListOfDoctors() {
        // given
        String specialization = "Cardiology";
        List<Doctor> doctors = List.of(buildDoctor(), buildDoctor().withDoctorId(2));
        when(doctorDAO.findBySpecializationName(specialization)).thenReturn(doctors);

        // when
        List<Doctor> result = doctorService.findDoctorsBySpecializationName(specialization);

        // then
        assertEquals(2, result.size());
        assertEquals(doctors, result);
    }

    @Test
    void saveSpecToDoctorShouldThrowIfDoctorAlreadyHadThatSpecialization() {
        Specialization specialization = Specialization.builder().specializationId(1).name("Cardiology").build();
        Specialization specializationDoubled = Specialization.builder().specializationId(1).name("Cardiology").build();
        Doctor doctor = buildDoctor().withSpecializations(Set.of(specialization));

        // when, then
        assertThrows(ProcessingException.class, () -> doctorService.saveSpecToDoctor(doctor, specializationDoubled));
    }

    @Test
    void saveSpecToDoctorShouldInvokeDoctorDAOSaveSpecToDoctor() {
        // given
        Specialization specialization = Specialization.builder().name("NewSpecialization").build();
        Doctor doctor = buildDoctor().withSpecializations(Set.of(specialization));
//        when(doctorDAO.findBySpecializationName(specialization.getName())).thenReturn(List.of());

        // when
        doctorService.saveSpecToDoctor(doctor, specialization);

        // then
        verify(doctorDAO).saveSpecToDoctor(doctor, specialization);
    }

    @Test
    void saveShouldInvokeDoctorDAOSave() {
        // given
        Doctor doctor = buildDoctor();

        // when
        doctorService.save(doctor);

        // then
        verify(doctorDAO).save(doctor);
    }
}
