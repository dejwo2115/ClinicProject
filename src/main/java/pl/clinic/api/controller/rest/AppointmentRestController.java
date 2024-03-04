package pl.clinic.api.controller.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.AppointmentProcessFormDTO;
import pl.clinic.api.dto.mapper.AppointmentMapper;
import pl.clinic.business.AppointmentService;
import pl.clinic.business.DoctorAvailabilityService;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/appointment/")
public class AppointmentRestController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final AppointmentMapper appointmentMapper;
    @SuppressWarnings("unused")
    @Operation(summary = "Get active appointments for patient by patient email")
    @GetMapping("/patient/request/active/{email}")
    public List<AppointmentDTO> getAppointmentForPatient(@RequestParam String email) {
        Patient patientByEmail = patientService.findPatientByEmail(email);
        return appointmentService.findByPatientEmailAndIsFinishedFalse(email)
                .stream().map(appointmentMapper::map).toList();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Get finished appointments for patient by patient email")
    @GetMapping("/patient/request/finished/{email}")
    public List<AppointmentDTO> getAllAppointmentForPatient(@RequestParam String email) {
        Patient patientByEmail = patientService.findPatientByEmail(email);
        return appointmentService.findByPatientEmailAndIsFinishedTrue(email)
                .stream().map(appointmentMapper::map).toList();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Create appointment request for patient for appointment.",
            description = "Insert your email. And issue availability(found by id) to appointment.")
    @PostMapping("/patient/makeRequest/{email}")
    public ResponseEntity<String> makeAppointmentRequest(
            @RequestParam("Patient email") String email,
            @RequestParam Integer availabilityId
    ) {
        Patient patientByEmail = patientService.findPatientByEmail(email);
        DoctorAvailability availability = doctorAvailabilityService.findById(availabilityId);
        if(patientByEmail != null && availability != null) {
            appointmentService.makeAppointmentRequest(patientByEmail, availability);
            return ResponseEntity.ok("Appointment request made.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect data given.");
        }
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Cancel appointment request for patient by deleting it.",
            description = "Given email will check if appointment is issued to this patient.")
    @DeleteMapping("/patient/request/remove/{email}")
    public ResponseEntity<String> cancelAppointment(
            @RequestParam("Patient email") String email,
            @RequestParam("appointmentId") Integer appointmentId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        Patient patient = patientService.findPatientByEmail(email);
        if(appointment != null && appointment.getPatient().equals(patient)) {
            appointmentService.cancelAppointment(appointment);
            return ResponseEntity.ok("Appointment successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect data given.");
        }
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Doctor process appointment.")
    @PostMapping("/doctor/appointment/process/{email}")
    public ResponseEntity<String> processAppointment(
            @RequestParam("Doctor email") String doctorEmail,
            @RequestParam("Appointment id") Integer appointmentId,
            @Valid @RequestBody AppointmentProcessFormDTO app
            ) {
        Doctor doctor = doctorService.findByEmail(doctorEmail);
        Appointment appointment = appointmentService.findById(appointmentId).withNote(app.getAppointmentNote());
        Patient patient = patientService.findPatientByEmail(appointment.getPatient().getEmail());
        Prescription prescription = Prescription.builder()
                .prescribedMedications(app.getPrescribedMedications())
                .note(app.getPrescriptionNote())
                .build();
        if(appointment.getDoctorAvailability().getDoctor().equals(doctor) && appointment.getPatient().equals(patient)) {
            appointmentService.processAppointment(prescription, appointment);
            return ResponseEntity.ok("Appointment proceed.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect data given.");
        }
    }
}
