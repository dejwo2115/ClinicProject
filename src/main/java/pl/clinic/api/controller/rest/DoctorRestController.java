package pl.clinic.api.controller.rest;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.clinic.api.dto.DoctorRestDTO;
import pl.clinic.api.dto.DoctorsDTO;
import pl.clinic.api.dto.SpecializationDTO;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.business.DoctorService;
import pl.clinic.business.SpecializationService;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorRest;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class DoctorRestController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final SpecializationService specializationService;
    private final ClinicUserDetailsService clinicUserDetailsService;

    @SuppressWarnings("unused")
    @Operation(summary = "Find all available doctors", description = "Returns a list of doctors with their specialization")
    @GetMapping("/doctors")
    public DoctorsDTO findAll() {
        return DoctorsDTO.builder()
                .doctors(doctorService.findAll().stream()
                        .map(doctorMapper::map)
                        .toList())
                .build();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Find doctors by their specialization", description = "Returns all doctors with matching specialization")
    @GetMapping("/doctors/{specialization}")
    public DoctorsDTO findBySpecialization(@RequestParam String specialization) {
        return DoctorsDTO.builder()
                .doctors(doctorService.findDoctorsBySpecializationName(specialization).stream()
                        .map(doctorMapper::map)
                        .toList())
                .build();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Create new doctor with login access.", description = "Creating doctor with login access to doctor service.")
    @PostMapping("/doctor")
    public ResponseEntity<String> createDoctor(
            @Valid @RequestBody DoctorRestDTO doctorRestDTO
    ) {
        DoctorRest doctorRest = doctorMapper.map(doctorRestDTO);
        try {
            clinicUserDetailsService.createUserDoctor(doctorRest);
            return ResponseEntity.ok("User added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add user" + e.getMessage());
        }
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Add new specialization to you doctor profile.")
    @PutMapping("/doctor/specialization/{email}")
    public ResponseEntity<String> setNewSpecializationForDoctor(
            @RequestParam String email,
            @RequestBody SpecializationDTO specializationDTO
            ) {
        Specialization specialization = doctorMapper.mapSpec(specializationDTO);

        Boolean isNewSpecialization = specializationService.isNewSpecialization(specialization);
        if(isNewSpecialization) {
            return ResponseEntity.badRequest().body("Specialization does not exist in database.");
        } else {
            Doctor doctor = doctorService.findByEmail(email);
            doctorService.saveSpecToDoctor(doctor, specialization);
            return ResponseEntity.ok("Specialization issued to doctor successfully.");
        }
    }
}
