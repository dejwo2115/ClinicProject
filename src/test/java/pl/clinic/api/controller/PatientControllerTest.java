package pl.clinic.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.mapper.DoctorMapper;
import pl.clinic.api.dto.mapper.PatientMapper;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Patient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.clinic.util.TestClassesFixtures.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {
    @InjectMocks
    private PatientController patientController;
    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private ExtendedModelMap model;


    @Test
    void showDoctorListShouldShowView() {
        // given
        String patientEmail = "test@example.com";
        Patient patient = buildPatient();
        PatientDTO patientDTO = buildPatientDTO();
        List<Doctor> allDoctors = List.of(buildDoctor());
        List<DoctorDTO> doctorDTOs = List.of(buildDoctorDTO());
        when(patientService.findPatientByEmail(patientEmail)).thenReturn(patient);
        when(patientMapper.map(Mockito.any(Patient.class))).thenReturn(patientDTO);
        when(doctorService.findAll()).thenReturn(allDoctors);
        when(doctorMapper.mapDoctors(allDoctors)).thenReturn(doctorDTOs);

        // when
        String viewName = patientController.showDoctorList(model, patientEmail);

        // then
        assertEquals("selectDoctorForAppointment", viewName);
        verify(patientService).findPatientByEmail(patientEmail);
        verify(patientMapper).map(patient);
        verify(doctorService).findAll();
        verify(doctorMapper).mapDoctors(allDoctors);
    }

}