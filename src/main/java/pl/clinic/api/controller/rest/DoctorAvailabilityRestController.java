package pl.clinic.api.controller.rest;


import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.clinic.api.dto.DoctorAvailabilityDTO;
import pl.clinic.api.dto.DoctorAvailabilityRestDTO;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.business.DoctorAvailabilityService;
import pl.clinic.business.DoctorService;
import pl.clinic.domain.Doctor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest")
public class DoctorAvailabilityRestController {
    private final DoctorMapper doctorMapper;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final DoctorService doctorService;
    @SuppressWarnings("unused")
    @Operation(summary = "Get all doctor availabilities.", description = "Only not reserved will be shown.")
    @GetMapping("/doctorAvailabilities/{email}")
    public List<DoctorAvailabilityDTO> getAppointmentForPatient(
            @RequestParam("email") String email) {
        Doctor doctor = doctorService.findByEmail(email);
        return doctorAvailabilityService.findByDoctor(doctor).stream().map(doctorMapper::mapDA).toList();
    }
    @SuppressWarnings("unused")
    @Operation(summary = "Create availabilities for given date and start time.",
            description = "It will create given amount of visits with 30minutes time space between every visit." +
                          " Given email will find doctor and issue availabilities")
    @PostMapping("/doctorAvailabilities/add")
    public void createDoctorAvailability(
            @RequestParam("email") String email,
            @RequestBody DoctorAvailabilityRestDTO da
            ) {
        Doctor doctor = doctorService.findByEmail(email);
        doctorAvailabilityService.createAvailabilityForDay(da.getAvailabilityDate().toLocalDate(), da.getAvailabilityDate().toLocalTime(), da.getAmountOfVisits(), doctor);
    }
}




