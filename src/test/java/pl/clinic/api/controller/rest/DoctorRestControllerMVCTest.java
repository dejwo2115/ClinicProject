package pl.clinic.api.controller.rest;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.SpecializationDTO;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.business.DoctorService;
import pl.clinic.business.SpecializationService;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;
import pl.clinic.util.TestClassesFixtures;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(DoctorRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorRestControllerMVCTest {


    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private DoctorMapper doctorMapper;

    @MockBean
    private SpecializationService specializationService;
    @SuppressWarnings("unused")
    @MockBean
    private ClinicUserDetailsService clinicUserDetailsService;
    @InjectMocks
    private DoctorRestController doctorRestController;

    @Test
    void testFindAllDoctors() throws Exception {
        // given
        Doctor doctor1 = TestClassesFixtures.buildDoctor();
        Doctor doctor2 = TestClassesFixtures.buildDoctor().withDoctorId(2);
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        DoctorDTO doctorDTO1 = TestClassesFixtures.buildDoctorDTO();
        DoctorDTO doctorDTO2 = TestClassesFixtures.buildDoctorDTO();
        doctorDTO2.setName("Random");

        when(doctorService.findAll()).thenReturn(doctors);

        when(doctorMapper.map(Mockito.any(Doctor.class))).thenReturn(doctorDTO1, doctorDTO2);

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctors").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctors[0].email").value(doctorDTO1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctors[0].name").value(doctorDTO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctors[1].email").value(doctorDTO2.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctors[1].name").value(doctorDTO2.getName()));
    }

    @Test
    void testSetNewSpecializationForDoctor(){
        // given
        String email = "doctor@example.com";
        SpecializationDTO specializationDTO = SpecializationDTO.builder().name("SomeSpec").build();
        Specialization specialization = Specialization.builder().name("SomeSpec").build();

        when(doctorMapper.mapSpec(specializationDTO)).thenReturn(specialization);
        when(specializationService.isNewSpecialization(specialization)).thenReturn(true);

        // when
        ResponseEntity<String> responseEntity = doctorRestController.setNewSpecializationForDoctor(email, specializationDTO);

        // then
        verify(doctorMapper).mapSpec(specializationDTO);
        verify(specializationService).isNewSpecialization(specialization);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Specialization does not exist in database.", responseEntity.getBody());
    }
    @Test
    void testSetNewSpecializationForDoctorWillAdd() {
        // given
        String email = "doctor@example.com";
        SpecializationDTO specializationDTO = SpecializationDTO.builder().name("SomeSpec").build();
        Specialization specialization = Specialization.builder().name("SomeSpec").build();
        Doctor doctor = TestClassesFixtures.buildDoctor();

        when(doctorMapper.mapSpec(specializationDTO)).thenReturn(specialization);
        when(specializationService.isNewSpecialization(specialization)).thenReturn(false);
        when(doctorService.findByEmail(email)).thenReturn(doctor);

        // when
        ResponseEntity<String> responseEntity = doctorRestController.setNewSpecializationForDoctor(email, specializationDTO);

        // then
        verify(doctorMapper).mapSpec(specializationDTO);
        verify(specializationService).isNewSpecialization(specialization);
        verify(doctorService).findByEmail(email);
        verify(doctorService).saveSpecToDoctor(doctor, specialization);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Specialization issued to doctor successfully.", responseEntity.getBody());
    }
}