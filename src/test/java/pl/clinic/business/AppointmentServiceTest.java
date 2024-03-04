package pl.clinic.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.business.dao.AppointmentDAO;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.domain.exception.NotFoundException;
import pl.clinic.util.TestClassesFixtures;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.*;

@ExtendWith({MockitoExtension.class})
class AppointmentServiceTest {
    @InjectMocks
    private AppointmentService appointmentService;
    @Mock
    private AppointmentDAO appointmentDAO;


    @Test
    void makeAppointmentRequestWorksCorrectly() {
        // given
        Patient patient = buildPatient();
        DoctorAvailability doctorAvailability = buildDoctorAvailability();

        // when
        appointmentService.makeAppointmentRequest(patient, doctorAvailability);

        // then
        Mockito.verify(appointmentDAO, times(1)).makeAppointmentRequest(patient, doctorAvailability);
    }

    @Test
    void thatFindByPatientEmailAndIsFinishedFalseWillReturnEmptyListIfNoAppointmentsFound() {
        // given
        String patientEmail = "test@example.com";
        when(appointmentDAO.findByPatientEmailFinishedFalse(patientEmail)).thenReturn(List.of());

        // when
        List<Appointment> result = appointmentService.findByPatientEmailAndIsFinishedFalse(patientEmail);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
    @Test
    void thatFindByPatientEmailAndIsFinishedFalseShouldReturnAppointments() {
        // given
        String patientEmail = "test@example.com";
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment1 = TestClassesFixtures.buildNotProceededAppointment(doctorAvailability);
        Appointment appointment2 =
                TestClassesFixtures
                        .buildNotProceededAppointment(doctorAvailability
                                .withDoctorAvailabilityId(2)
                                .withAvailabilityDate(LocalDate.now().plusDays(6)));
        List<Appointment> appointments = Arrays.asList(appointment1, appointment2);
        when(appointmentDAO.findByPatientEmailFinishedFalse(patientEmail)).thenReturn(appointments);
        // when
        List<Appointment> result = appointmentService.findByPatientEmailAndIsFinishedFalse(patientEmail);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).contains(appointment1, appointment2);
    }

    @Test
    void findByIdShouldReturnExistingAppointment() {
        // Given
        int appointmentId = 1;
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment expectedAppointment =
                TestClassesFixtures.buildNotProceededAppointment(doctorAvailability).withAppointmentId(appointmentId);


        when(appointmentDAO.findById(appointmentId)).thenReturn(Optional.of(expectedAppointment));

        // When
        Appointment actualAppointment = appointmentService.findById(appointmentId);

        // Then
        assertEquals(expectedAppointment, actualAppointment);
    }
    @Test
    void findByIdShouldThrowNotFoundException() {
        // Given
        int appointmentId = 1;
        when(appointmentDAO.findById(appointmentId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> appointmentService.findById(appointmentId));
    }


    @Test
    void thatCancelAppointmentWorksCorrectly() {
        //given
        Appointment appointment = Appointment.builder().build();

        //when
        appointmentService.cancelAppointment(appointment);

        //then
        verify(appointmentDAO).cancelAppointment(appointment);
    }

    @Test
    void findByPatientEmailAndIsFinishedTrue() {
        //given
        Patient patient = buildPatient();
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Prescription prescription1 = buildPrescription(appointment);
        Prescription prescription2 = buildPrescription(appointment).withPrescriptionId(3).withPrescriptionCode("DifferentCode12312");
        Appointment proceededAppointment =
                appointment.withAppointmentId(2).withPrescription(prescription1).withNote("someNote").withIsFinished(true);
        Appointment proceededAppointment2 =
                appointment.withAppointmentId(2).withPrescription(prescription2).withNote("someNote").withIsFinished(true);
        when(appointmentDAO.findByPatientEmailAndIsFinishedTrue(patient.getEmail())).thenReturn(List.of(proceededAppointment2, proceededAppointment));
        //when
        List<Appointment> byPatientEmailAndIsFinishedTrue = appointmentService.findByPatientEmailAndIsFinishedTrue(patient.getEmail());

        //then
        assertEquals(2, byPatientEmailAndIsFinishedTrue.size());
        assertEquals(proceededAppointment2, byPatientEmailAndIsFinishedTrue.get(0));
        assertEquals(proceededAppointment, byPatientEmailAndIsFinishedTrue.get(1));
    }

    @Test
    void thatProcessAppointmentWorksCorrectly() {
        //given
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = buildPrescription(appointment);

        //when
        appointmentService.processAppointment(prescription,appointment);

        //then
        verify(appointmentDAO).processAppointment(any(Appointment.class));
        verify(appointmentDAO, times(1)).processAppointment(any(Appointment.class));
    }
    @Test
    void testGetAppointment() {
        // given
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = buildPrescription(appointment);
        // when
        String generatePrescriptionCode = appointmentService.generatePrescriptionCode(appointment);
        Appointment result = appointmentService.getAppointment(prescription, appointment);

        // then
        assertNotEquals(prescription.getPrescriptionCode(), generatePrescriptionCode);
        assertEquals(prescription, result.getPrescription());
        assertEquals(true, result.getIsFinished());
    }
    @Test
    void testRandomInt() {
        // given
        int min = 1;
        int max = 100;

        // when
        int random = appointmentService.randomInt(min, max);

        // then
        assert random >= min && random <= max : "Random number is not within the given range";
    }
}