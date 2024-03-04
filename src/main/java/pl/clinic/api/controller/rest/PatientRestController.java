package pl.clinic.api.controller.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.PatientRestDTO;
import pl.clinic.api.dto.mapper.PatientMapper;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Patient;
import pl.clinic.domain.PatientRest;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class PatientRestController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final ClinicUserDetailsService clinicUserDetailsService;
    @Operation(summary = "Get a patient by email", description = "Returns a single patient")
    @SuppressWarnings("unused")
    @GetMapping("/patient/{email}")
    public ResponseEntity<PatientDTO> getPatientByEmail(@RequestParam String email) {
        Patient patient = patientService.findPatientByEmail(email);
        PatientDTO patientDTO = patientMapper.map(patient);
        if(Objects.isNull(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patientDTO);
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Create new patient with login access.", description = "Creating patient with login access to patient service.")
    @PostMapping("/patient")
    public ResponseEntity<String> createPatient(@Valid @RequestBody PatientRestDTO patientRestDTO) {
            PatientRest patientRest = patientMapper.map(patientRestDTO);
        try {
            clinicUserDetailsService.createUserPatient(patientRest);
            return ResponseEntity.ok("User added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add user " + e.getMessage());
        }
    }

}
