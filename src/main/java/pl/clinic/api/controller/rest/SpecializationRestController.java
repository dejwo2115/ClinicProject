package pl.clinic.api.controller.rest;


import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.clinic.api.dto.SpecializationDTO;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.business.SpecializationService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class SpecializationRestController {
    private final DoctorMapper doctorMapper;
    private final SpecializationService specializationService;

    @SuppressWarnings("unused")
    @Operation(summary = "Get all doctor specializations.")
    @GetMapping("/specializations")
    public List<SpecializationDTO> getAppointmentForPatient() {
        return specializationService.findAll().stream().map(doctorMapper::mapSpec).toList();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Create new specialization.", description = "Creating new specialization if is not available in database.")
    @PostMapping("/specialization/{specialization}")
    public void createSpecialization(@RequestParam("specialization") String specialization) {
        specializationService.save(specialization);
    }
}




