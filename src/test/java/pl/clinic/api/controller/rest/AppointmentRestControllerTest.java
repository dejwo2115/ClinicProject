package pl.clinic.api.controller.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.mapper.AppointmentMapper;
import pl.clinic.business.AppointmentService;
import pl.clinic.business.DoctorAvailabilityService;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Patient;

import java.util.List;

import static org.mockito.Mockito.when;
import static pl.clinic.util.TestClassesFixtures.*;

@WebMvcTest(AppointmentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AppointmentRestControllerTest {
    @SuppressWarnings("unused")
    private final MockMvc mockMvc;
    @MockBean
    @SuppressWarnings("unused")
    private PatientService patientService;
    @MockBean
    @SuppressWarnings("unused")
    private DoctorService doctorService;
    @MockBean
    @SuppressWarnings("unused")
    private AppointmentService appointmentService;
    @MockBean
    @SuppressWarnings("unused")
    private DoctorAvailabilityService doctorAvailabilityService;
    @MockBean
    @SuppressWarnings("unused")
    private AppointmentMapper appointmentMapper;

    @Test
    void getAppointmentForPatient() throws Exception {
        // given
        String email = "maciejwolski@gmail.com";
        Patient patient = buildPatient().withEmail(email);
        Appointment appointment = buildNotProceededAppointment(buildDoctorAvailability());
        AppointmentDTO appointmentDTO = buildNotProceededAppointmentDTO(buildDoctorAvailabilityDTO());
        List<Appointment> appointments = List.of(appointment);
        when(patientService.findPatientByEmail(email)).thenReturn(patient);
        when(appointmentService.findByPatientEmailAndIsFinishedFalse(email)).thenReturn(appointments);
        when(appointmentMapper.map(appointment)).thenReturn(appointmentDTO);
    }
}