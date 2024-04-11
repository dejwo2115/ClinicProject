package pl.clinic.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import pl.clinic.domain.*;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PatientController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final AppointmentMapper appointmentMapper;
    private final ClinicUserDetailsService clinicUserDetailsService;


    @SuppressWarnings("unused")
    @GetMapping("/patient")
    public String getHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            String userName = authentication.getName();
            Patient patientByUserName = clinicUserDetailsService.findPatientByUserName(userName);
            PatientDTO patientDTO = patientMapper.map(patientByUserName);
            model.addAttribute("patient", patientDTO);

        } else {
            Patient patient = patientService.findAll().get(0);
            PatientDTO patientDTO2 = patientMapper.map(patient);
            model.addAttribute("patient", patientDTO2);
        }


        return "patientHome";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/selectDoctorForAppointment/{patientEmail}")
    public String showDoctorList(Model model, @PathVariable("patientEmail") String patientEmail) {
        List<Doctor> all = doctorService.findAll();
        List<DoctorDTO> doctorDTOS = doctorMapper.mapDoctors(all);
        Patient patient = patientService.findPatientByEmail(patientEmail);
        PatientDTO patientDTO = patientMapper.map(patient);

        model.addAttribute("patient", patientDTO);
        model.addAttribute("doctorWithSpecializations", doctorDTOS);
        return "selectDoctorForAppointment";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/makeAppointment/dates")
    public String selectDoctor(@RequestParam("doctorEmail") String doctorEmail,
                               @RequestParam("patientEmail") String patientEmail,
                               Model model) {
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        List<LocalDate> availableDates = doctorAvailabilityService.findByDoctor(doctor).stream()
                .filter(x -> !x.getReserved())
                .map(DoctorAvailability::getAvailabilityDate)
                .distinct()
                .toList();
        model.addAttribute("doctorEmail", doctorEmail);
        model.addAttribute("patientEmail", patientEmail);
        model.addAttribute("availableDates", availableDates);
        return "selectDate";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/makeAppointment/dates/selectTime")
    public String selectTime(@RequestParam("doctorEmail") String doctorEmail,
                             @RequestParam("patientEmail") String patientEmail,
                             @RequestParam("selectedDate") LocalDate selectedDate,
                             Model model) {
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        List<LocalTime> availableTimes = doctorAvailabilityService.findTimesForDate(doctor, selectedDate).stream().sorted().toList();
        model.addAttribute("doctorEmail", doctorEmail);
        model.addAttribute("patientEmail", patientEmail);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableTimes", availableTimes);
        return "selectTime";
    }
    @SuppressWarnings("unused")
    @PostMapping("/patient/confirmAppointment")
    public String confirmAppointment(@RequestParam("doctorEmail") String doctorEmail,
                                     @RequestParam("patientEmail") String patientEmail,
                                     @RequestParam("selectedDate") LocalDate selectedDate,
                                     @RequestParam("selectedTime") LocalTime selectedTime,
                                     RedirectAttributes redirectAttributes) {
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        DoctorAvailability doctorAvailability = doctorAvailabilityService.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime);
        Patient patient = patientService.findPatientByEmail(patientEmail);
        appointmentService.makeAppointmentRequest(patient, doctorAvailability);
        redirectAttributes.addFlashAttribute("message", "Appointment has been successfully made.");
        return "redirect:/successMessage";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/historyAndUpcomingVisitsPage/{patientEmail}")
    public String getPatientHistoryAndUpcomingVisitsPage(Model model, @PathVariable("patientEmail") String patientEmail) {
        Patient patient = patientService.findPatientByEmail(patientEmail);
        PatientDTO patientDTO = patientMapper.map(patientService.findPatientByEmail(patientEmail));

        model.addAttribute("patient", patientDTO);
        return "patientHistoryAndUpcomingPage";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/history/{patientEmail}")
    public String getPatientHistory(Model model, @PathVariable("patientEmail") String patientEmail) {
        Patient patient = patientService.findPatientByEmail(patientEmail);
        PatientDTO patientDTO = patientMapper.map(patientService.findPatientByEmail(patientEmail));
        List<Appointment> appointmentList = appointmentService.findByPatientEmailAndIsFinishedTrue(patientEmail);
        List<AppointmentDTO> appointments = appointmentMapper.map(appointmentList);

        model.addAttribute("appointments", appointments);
        model.addAttribute("patient", patientDTO);
        return "patientHistoryPage";
    }
    @SuppressWarnings("unused")
    @GetMapping("/patient/history/appointment/{appointmentId}")
    public String getDetailsForAppointment(
            Model model,
            @PathVariable("appointmentId") Integer appointmentId
    ) {
        Appointment appointment = appointmentService.findById(appointmentId);
        Prescription prescription = appointment.getPrescription();
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        PrescriptionDTO prescriptionDTO = appointmentMapper.map(prescription);
        Doctor doctor = appointment.getDoctorAvailability().getDoctor();
        DoctorDTO map = doctorMapper.map(doctor);
        model.addAttribute("doctor", map);
        model.addAttribute("prescription", prescriptionDTO);
        model.addAttribute("appointment", appointmentDTO);
        return "appointmentDetails";
    }
    @SuppressWarnings("unused")
    @GetMapping("patient/upcomingVisits/{patientEmail}")
    public String getPatientUpcomingVisits(Model model, @PathVariable("patientEmail") String patientEmail) {
        Patient patient = patientService.findPatientByEmail(patientEmail);
        PatientDTO patientDTO = patientMapper.map(patient);
        Map<AppointmentDTO, DoctorDTO> appointmentDoctorMap = prepareDataAsMap(patient);

        model.addAttribute("patient", patientDTO);
        model.addAttribute("appointmentDoctorMap", appointmentDoctorMap);
        return "patientUpcomingVisitsPage";
    }
    @SuppressWarnings("unused")
    @DeleteMapping("patient/upcomingVisits/cancel")
    public String cancelVisit(
            @RequestParam("appointmentId") Integer appointmentId,
            RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.findById(appointmentId);
        appointmentService.cancelAppointment(appointment);

        redirectAttributes.addFlashAttribute("message", "Appointment has been successfully cancelled.");
        return "redirect:/successMessage";
    }
    private Map<AppointmentDTO, DoctorDTO> prepareDataAsMap(Patient patient) {
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

