package pl.clinic.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AppointmentController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentService appointmentService;

    @SuppressWarnings("unused")
    @GetMapping("/doctor/process/patientList")
    public String showListOfPatients(Model model) {
        List<Patient> patientList = patientService.findAll();
        List<PatientDTO> patients = patientList.stream().map(patientMapper::map).toList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        model.addAttribute("patients", patients);
        return "appointmentProcessPatientList";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/process/patientList/details/{patientEmail}")
    public String showPatientDetailsByEmail(
            @PathVariable("patientEmail") String email,
            Model model) {
        Patient patient = patientService.findPatientByEmail(email);
        PatientDTO patientDTO = patientMapper.map(patient);
        Map<AppointmentDTO, DoctorDTO> appointmentDoctorMap = prepareDataAsMap(patient);
        model.addAttribute("patient", patientDTO);
        model.addAttribute("appointmentDoctorMap", appointmentDoctorMap);
        return "patientDetailsAndAppointments";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/process/patientDetailsAndAppointments")
    public String showAppointmentProcessForm(Model model,
                                             @RequestParam("patientEmail") String patientEmail,
                                             @RequestParam("doctorEmail") String doctorEmail,
                                             @RequestParam("appointmentId") Integer appointmentId
    ) {
        Appointment appointment = appointmentService.findById(appointmentId);
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        Patient patient = patientService.findPatientByEmail(patientEmail);
        PatientDTO patientDTO = patientMapper.map(patient);
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        DoctorDTO doctorDTO = doctorMapper.map(doctor);

        model.addAttribute("patientDTO", patientDTO);
        model.addAttribute("doctorDTO", doctorDTO);
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("prescriptionNote", "");
        model.addAttribute("prescribedMedications", "");
        model.addAttribute("appointmentNote", "");
        return "processAppointment";
    }

    @SuppressWarnings("unused")
    @PostMapping("/doctor/process/processAppointment")
    public String processAppointment(@RequestParam("prescribedMedications") String prescribedMedications,
                                     @RequestParam("prescriptionNote") String prescriptionNote,
                                     @RequestParam("appointmentNote") String appointmentNote,
                                     @RequestParam("appointmentId") Integer appointmentId,
                                     @RequestParam("patientEmail") String patientEmail,
                                     @RequestParam("doctorEmail") String doctorEmail,
                                     RedirectAttributes redirectAttributes
    ) {
        Appointment appointment = appointmentService.findById(appointmentId).withNote(appointmentNote);
        PrescriptionDTO prescriptionDTO = PrescriptionDTO.builder().note(prescriptionNote).prescribedMedications(prescribedMedications).build();
        Prescription prescription = appointmentMapper.map(prescriptionDTO);
        appointmentService.processAppointment(prescription, appointment);
        redirectAttributes.addFlashAttribute("message", "Appointment has been successfully processed.");
        return "redirect:/successMessage";
    }
    public Map<AppointmentDTO, DoctorDTO> prepareDataAsMap(Patient patient) {
        List<Appointment> appointments = appointmentService.findByPatientEmailAndIsFinishedFalse(patient.getEmail()).stream().filter(x -> !x.getCanceled()).toList();
        Map<AppointmentDTO, DoctorDTO> appointmentDoctorMap = new LinkedHashMap<>();
        for (Appointment appointment : appointments) {
            AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
            Doctor doctor = appointment.getDoctorAvailability().getDoctor();
            DoctorDTO doctorDTO = doctorMapper.map(doctor);
            appointmentDoctorMap.put(appointmentDTO, doctorDTO);
        }
        return appointmentDoctorMap;
    }
}
