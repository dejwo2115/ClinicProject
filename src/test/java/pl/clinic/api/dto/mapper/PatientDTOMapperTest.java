package pl.clinic.api.dto.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.PatientRestDTO;
import pl.clinic.domain.Patient;
import pl.clinic.domain.PatientRest;
import pl.clinic.util.TestClassesFixtures;

import java.util.List;

class PatientDTOMapperTest {
    private final PatientMapper patientMapper = new PatientMapperImpl();

    @Test
    void map() {
        Patient patient = TestClassesFixtures.buildPatient();
        Patient patient2 = TestClassesFixtures.buildPatient().withPatientId(2);
        List<Patient> patient21 = List.of(patient2, patient);
        PatientRest patientRest = PatientRest.builder().name("some1").build();
        PatientRestDTO patientRestDTO = PatientRestDTO.builder().name("some1").build();

        PatientDTO mappedPatientDTO = patientMapper.map(patient);
        PatientRestDTO mappedPatientRestDTO1 = patientMapper.map(patientRest);
        PatientRest mappedPatientRest1 = patientMapper.map(patientRestDTO);
        List<PatientDTO> mappedList = patientMapper.map(patient21);

        Assertions.assertThat(mappedList).isNotNull();
        Assertions.assertThat(mappedPatientDTO).isNotNull();
        Assertions.assertThat(mappedPatientRestDTO1).isNotNull();
        Assertions.assertThat(mappedPatientRest1).isNotNull();

    }
    @Test
    void mapNullWillReturnNull(){

        //given, when
        PatientDTO mappedPatientDTO = patientMapper.map((Patient) null);
        PatientRestDTO mappedPatientRestDTO1 = patientMapper.map((PatientRest) null);
        PatientRest mappedPatientRest1 = patientMapper.map((PatientRestDTO) null);
        //then
        Assertions.assertThat(mappedPatientDTO).isNull();
        Assertions.assertThat(mappedPatientRestDTO1).isNull();
        Assertions.assertThat(mappedPatientRest1).isNull();
    }
}