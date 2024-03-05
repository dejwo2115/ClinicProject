package pl.clinic.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.PrescriptionDTO;
import pl.clinic.api.dto.mapper.AppointmentMapper;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.api.dto.mapper.PatientMapper;
import pl.clinic.business.AppointmentService;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {
    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private DoctorService doctorService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentController appointmentController;

    @Test
    void thatShowListOfPatientsWorksCorrectly() {
        //given
        List<Patient> patients = List.of(
                Patient.builder().name("John").surname("Big").email("john@example.com").birthDate(LocalDate.of(1990, 5, 15)).gender("Male").phone("+48 123 456 789").build(),
                Patient.builder().name("Alice").surname("Small").email("alice@example.com").birthDate(LocalDate.of(1985, 8, 22)).gender("Female").phone("+48 987 654 321").build()
        );
        Mockito.when(patientMapper.map(Mockito.any(Patient.class))).thenAnswer(invocation -> {
            Patient patient = invocation.getArgument(0);
            return PatientDTO.builder()
                    .name(patient.getName())
                    .surname(patient.getSurname())
                    .email(patient.getEmail())
                    .birthDate(patient.getBirthDate())
                    .gender(patient.getGender())
                    .phone(patient.getPhone())
                    .build();
        });
        List<PatientDTO> patientsDto = patients.stream().map(patientMapper::map).collect(Collectors.toList());
        ExtendedModelMap model = new ExtendedModelMap();

        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Mockito.when(patientService.findAll()).thenReturn(patients);
        //when
        String result = appointmentController.showListOfPatients(model);

        //then
        assertThat(result).isEqualTo("appointmentProcessPatientList");
        assertThat(model.getAttribute("patients")).isEqualTo(patientsDto);

    }

    @Test
    void showPatientDetailsByEmail() {
        // given
        String email = "test@example.com";
        Patient patient = buildPatient().withEmail(email);
        PatientDTO patientDTO = new PatientDTO();
        when(patientService.findPatientByEmail(email)).thenReturn(patient);
        when(patientMapper.map(patient)).thenReturn(patientDTO);

        List<Appointment> appointments = Collections.emptyList();
        when(appointmentService.findByPatientEmailAndIsFinishedFalse(email)).thenReturn(appointments);

        Map<AppointmentDTO, DoctorDTO> appointmentDoctorMap = Collections.emptyMap();

        Model model = mock(Model.class);

        // when
        String viewName = appointmentController.showPatientDetailsByEmail(email, model);

        // then
        assertEquals("patientDetailsAndAppointments", viewName);
        verify(model).addAttribute("patient", patientDTO);
        verify(model).addAttribute("appointmentDoctorMap", appointmentDoctorMap);
    }

    @Test
    void prepareDataAsMap_ReturnsCorrectMap() {
        // given
        Patient patient = buildPatient().withEmail("test@example.com");

        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Doctor doctor = buildDoctor();

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        DoctorDTO doctorDTO = new DoctorDTO();

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);

        when(appointmentService.findByPatientEmailAndIsFinishedFalse(patient.getEmail())).thenReturn(appointments);
        when(appointmentMapper.map(appointment)).thenReturn(appointmentDTO);
        when(doctorMapper.map(doctor)).thenReturn(doctorDTO);

        // when
        Map<AppointmentDTO, DoctorDTO> result = appointmentController.prepareDataAsMap(patient);

        // then
        assertEquals(1, result.size());
        assertTrue(result.containsKey(appointmentDTO));
        assertEquals(doctorDTO, result.get(appointmentDTO));
    }

    //
//
    @Test
    void showAppointmentProcessForm() {
        // given
        String patientEmail = "patient@example.com";
        String doctorEmail = "doctor@example.com";
        int appointmentId = 1;

        Appointment appointment = buildNotProceededAppointment(buildDoctorAvailability()); // przykładowy obiekt Appointment
        when(appointmentService.findById(appointmentId)).thenReturn(appointment);

        AppointmentDTO appointmentDTO = buildNotProceededAppointmentDTO(buildDoctorAvailabilityDTO());
        when(appointmentMapper.map(appointment)).thenReturn(appointmentDTO);

        Patient patient = buildPatient();
        when(patientService.findPatientByEmail(patientEmail)).thenReturn(patient);

        PatientDTO patientDTO = new PatientDTO();
        when(patientMapper.map(patient)).thenReturn(patientDTO);

        Doctor doctor = buildDoctor();
        when(doctorService.findByEmail(doctorEmail)).thenReturn(doctor);

        DoctorDTO doctorDTO = new DoctorDTO();
        when(doctorMapper.map(doctor)).thenReturn(doctorDTO);

        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String viewName = appointmentController.showAppointmentProcessForm(model, patientEmail, doctorEmail, appointmentId);

        // then
        assertThat(model.getAttribute("patientDTO")).isEqualTo(patientDTO);
        assertThat(model.getAttribute("doctorDTO")).isEqualTo(doctorDTO);
        assertThat(model.getAttribute("appointmentDTO")).isEqualTo(appointmentDTO);
        assertThat(model.getAttribute("prescriptionNote")).isEqualTo("");
        assertThat(model.getAttribute("prescribedMedications")).isEqualTo("");
        assertThat(model.getAttribute("appointmentNote")).isEqualTo("");
        assertEquals("processAppointment", viewName);
    }

    //
    @Test
    void processAppointment() {
        // given
        Integer appointmentId = 1;
        String appointmentNote = "Test note";
        String prescriptionNote = "Prescription note";
        String prescribedMedications = "Medication 1, Medication 2";
        String patientEmail = "test@example.com";
        String doctorEmail = "doctor@example.com";

        Appointment appointment = buildNotProceededAppointment(buildDoctorAvailability());
        when(appointmentService.findById(appointmentId)).thenReturn(appointment);

        PrescriptionDTO prescriptionDTO = PrescriptionDTO.builder()
                .note(prescriptionNote)
                .prescribedMedications(prescribedMedications)
                .build();
        Prescription prescription = buildPrescription(appointment);
        when(appointmentMapper.map(prescriptionDTO)).thenReturn(prescription);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String redirectUrl = appointmentController.processAppointment(
                prescribedMedications,
                prescriptionNote,
                appointmentNote,
                appointmentId,
                patientEmail,
                doctorEmail,
                redirectAttributes
        );

        // then
        verify(appointmentService).processAppointment(prescription, appointment);
        verify(redirectAttributes).addFlashAttribute("message", "Appointment has been successfully processed.");
        assertEquals("redirect:/successMessage", redirectUrl);
    }
}
