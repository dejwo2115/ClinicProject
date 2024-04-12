package pl.clinic.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.mapper.AppointmentMapper;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.api.dto.mapper.PatientMapper;
import pl.clinic.business.AppointmentService;
import pl.clinic.business.DoctorAvailabilityService;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Patient;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {
    @InjectMocks
    private DoctorController doctorController;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private ExtendedModelMap extendedModelMap;
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private ClinicUserDetailsService clinicUserDetailsService;

    @Test
    void doctorHome() {
        // given, when
        String viewName = doctorController.doctorHome();

        // then
        assertEquals("doctorHome", viewName);
    }

    @Test
    void showAddAvailabilityForm() {
        // given
        String userName = "testDoctor";
        Doctor doctor = buildDoctor();
        when(clinicUserDetailsService.findDoctorByUserName(authentication.getName())).thenReturn(doctor);



        // when
        String viewName = doctorController.showAddAvailabilityForm(extendedModelMap, authentication);

        // then
        assertEquals("makeAvailability", viewName);
        verify(extendedModelMap).addAttribute(Mockito.eq("doctor"), Mockito.any());
    }

    @Test
    void addAvailability() {
        // given
        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        Integer amountOfVisits = 5;
        String doctorEmail = "test@example.com";
        Doctor doctor = buildDoctor();
        when(doctorService.findByEmail(doctorEmail)).thenReturn(doctor);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        // when
        String redirectUrl = doctorController.addAvailability(date, startTime, amountOfVisits, doctorEmail, redirectAttributes);

        // then
        assertEquals("redirect:/successMessage", redirectUrl);
        verify(doctorAvailabilityService).createAvailabilityForDay(date, startTime, amountOfVisits, doctor);
        verify(redirectAttributes).addFlashAttribute(Mockito.eq("message"), anyString());
    }

    @Test
    void getPatientList() {
        // given
        List<PatientDTO> patients = List.of(PatientDTO.builder().build());
        List<Patient> patientsList = List.of(buildPatient());
        when(patientService.findAll()).thenReturn(patientsList);
        when(patientMapper.map(patientsList)).thenReturn(patients);


        // when
        String viewName = doctorController.getPatientList(extendedModelMap);

        // then
        assertEquals("doctorPatientList", viewName);
        verify(extendedModelMap).addAttribute("patients", patients);
    }

    @Test
    void getPatientHistory() {
        // given
        String patientEmail = "test@example.com";
        Patient patient = buildPatient().withEmail(patientEmail);
        List<Appointment> appointmentsList = List.of(buildNotProceededAppointment(buildDoctorAvailability()));
        AppointmentDTO appointmentDTO = buildNotProceededAppointmentDTO(buildDoctorAvailabilityDTO());
        List<AppointmentDTO> appointments = List.of(appointmentDTO);

        when(patientService.findPatientByEmail(patientEmail)).thenReturn(patient);
        when(appointmentMapper.map(appointmentsList)).thenReturn(appointments);
        when(appointmentService.findByPatientEmailAndIsFinishedTrue(patientEmail)).thenReturn(appointmentsList);


        // when
        String viewName = doctorController.getPatientHistory(patientEmail, extendedModelMap);

        // then
        assertEquals("doctorPatientHistoryPage", viewName);
        verify(extendedModelMap).addAttribute("appointments", appointments);
    }

    @Test
    void getDetailsForAppointment() {
        // given
        Integer id = 1;
        Appointment appointment = buildNotProceededAppointment(buildDoctorAvailability());
        Doctor doctor = appointment.getDoctorAvailability().getDoctor();

        AppointmentDTO appointmentDTO = buildNotProceededAppointmentDTO(buildDoctorAvailabilityDTO());
        DoctorDTO doctorDTO = buildDoctorDTO();

        when(appointmentService.findById(id)).thenReturn(appointment);
        when(appointmentMapper.map(appointment)).thenReturn(appointmentDTO);
        when(doctorMapper.map(doctor)).thenReturn(doctorDTO);

        // when
        String viewName = doctorController.getDetailsForAppointment(extendedModelMap, id);

        // then
        assertEquals("appointmentDetails", viewName);
        verify(extendedModelMap).addAttribute("appointment", appointmentDTO);
        verify(extendedModelMap).addAttribute("doctor", doctorDTO);


    }
}