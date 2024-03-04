package pl.clinic.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.business.dao.PatientDAO;
import pl.clinic.domain.Address;
import pl.clinic.domain.Patient;
import pl.clinic.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.buildPatient;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientDAO patientDAO;

    @InjectMocks
    private PatientService patientService;

    @Test
    void savingPatientWorksCorrectly() {
        //given
        Patient patient = buildPatient();
        //when
        patientService.savePatient(patient);
        //then
        verify(patientDAO, times(1)).savePatient(patient);
    }

    @Test
    void findingPatientByEmailWorksCorrectly() {
        //given
        String email = "123email123@gmail.com";
        Patient patient = buildPatient().withEmail(email);
        //when
        when(patientDAO.findByEmail(email)).thenReturn(Optional.ofNullable(patient));
        Patient found = patientService.findPatientByEmail(email);

        assertEquals(found.getEmail(), patient.getEmail());
    }

    @Test
    void findingPatientWithNonExistingEmailThrowsException() {
        //given
        String email = "badEmail";
        //when

        when(patientDAO.findByEmail(email)).thenReturn(Optional.empty());

        //then

        Throwable notFoundException = assertThrows(NotFoundException.class, () -> patientService.findPatientByEmail(email));
        assertEquals("Could not find patient by email: [%s]".formatted(email), notFoundException.getMessage());

    }
    @Test
    void thatFindAllWillReturnListOfPatients() {
        //given
        Patient patient = buildPatient();
        Address address2 = patient.getAddress().withAddressId(2);
        Patient patient2 = buildPatient().withPatientId(1).withAddress(address2);
        //when
        patientService.savePatient(patient);
        patientService.savePatient(patient2);
        when(patientDAO.findAll()).thenReturn(List.of(patient2,patient));
        List<Patient> allFound = patientService.findAll();
        //then
        assertEquals(allFound.size(), 2);
        assertFalse(allFound.isEmpty());
    }


}