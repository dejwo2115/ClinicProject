package pl.clinic.api.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
import pl.clinic.business.DoctorAvailabilityService;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorMapper doctorMapper;
    private final DoctorService doctorService;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;
    private final ClinicUserDetailsService clinicUserDetailsService;

    @SuppressWarnings("unused")
    @GetMapping("/doctor")
    public String doctorHome() {
        return "doctorHome";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/makeAvailability")
    public String showAddAvailabilityForm(Model model, Authentication authentication) {

        Doctor doctorByUserName = clinicUserDetailsService.findDoctorByUserName(authentication.getName());
        DoctorDTO doctor = doctorMapper.map(doctorByUserName);
        model.addAttribute("doctor", doctor);

        return "makeAvailability";
    }

    @SuppressWarnings("unused")
    @PostMapping("/doctor/addAvailability")
    public String addAvailability(@RequestParam("date") LocalDate date,
                                  @RequestParam("startTime") LocalTime startTime,
                                  @RequestParam("amountOfVisits") Integer amountOfVisits,
                                  @RequestParam("doctorEmail") String doctorEmail,
                                  RedirectAttributes redirectAttributes
    ) {
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        doctorAvailabilityService.createAvailabilityForDay(date, startTime, amountOfVisits, doctor);
        String message = "Availability added successfully.";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/successMessage";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/history/patientList")
    public String getPatientList(Model model) {
        List<PatientDTO> patients = patientMapper.map(patientService.findAll());

        model.addAttribute("patients", patients);

        return "doctorPatientList";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/history/patientList/details/{patientEmail}")
    public String getPatientHistory(@PathVariable("patientEmail") String patientEmail, Model model) {
        Patient patient = patientService.findPatientByEmail(patientEmail);
        List<AppointmentDTO> appointments = appointmentMapper.map(appointmentService.findByPatientEmailAndIsFinishedTrue(patientEmail));

        model.addAttribute("appointments", appointments);
        return "doctorPatientHistoryPage";
    }

    @SuppressWarnings("unused")
    @GetMapping("/doctor/history/appointment/{appointmentId}")
    public String getDetailsForAppointment(
            Model model,
            @PathVariable("appointmentId") Integer appointmentId
    ) {
        Appointment appointment = appointmentService.findById(appointmentId);
        Prescription prescription = appointment.getPrescription();
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        PrescriptionDTO prescriptionDTO = appointmentMapper.map(prescription);
        Doctor doctor = appointment.getDoctorAvailability().getDoctor();
        DoctorDTO doctorDTO = doctorMapper.map(doctor);
        model.addAttribute("doctor", doctorDTO);
        model.addAttribute("prescription", prescriptionDTO);
        model.addAttribute("appointment", appointmentDTO);
        return "appointmentDetails";
    }
}

