package pl.clinic.api.dto.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.api.dto.DoctorAvailabilityDTO;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.DoctorRestDTO;
import pl.clinic.api.dto.SpecializationDTO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.DoctorRest;
import pl.clinic.domain.Specialization;

import java.util.List;
import java.util.Set;

import static pl.clinic.util.TestClassesFixtures.buildDoctor;
import static pl.clinic.util.TestClassesFixtures.buildDoctorAvailability;

class DoctorDTOMapperTest {
    private final DoctorMapper doctorMapper = new DoctorMapperImpl();

    @Test
    void doctorMapperTest() {
        //given
        Doctor doctor1 = buildDoctor();
        Doctor doctor2 = buildDoctor().withDoctorId(3);
        List<Doctor> doctorList = List.of(doctor2, doctor1);
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        DoctorRestDTO doctorRestDTO = DoctorRestDTO.builder().email("someEmail").build();
        Specialization spec1 = Specialization.builder().name("spec1").build();
        Specialization spec2 = Specialization.builder().name("spec2").build();
        Set<Specialization> specializationSet = Set.of(spec1.withSpecializationId(1),spec2.withSpecializationId(2));
        SpecializationDTO specDTO = SpecializationDTO.builder().name("spec2").build();
        //when
        Specialization specialization = doctorMapper.mapSpec(specDTO);
        DoctorDTO doctorDTO = doctorMapper.map(doctor1);
        List<DoctorDTO> doctorDTOS = doctorMapper.mapDoctors(doctorList);
        DoctorAvailabilityDTO doctorAvailabilityDTO = doctorMapper.mapDA(doctorAvailability);
        SpecializationDTO spec = doctorMapper.mapSpec(spec1);
        Set<SpecializationDTO> specializationDTOSet = doctorMapper.map(specializationSet);
        DoctorRest doctorRest = doctorMapper.map(doctorRestDTO);
        //then
        Assertions.assertNotNull(specialization);
        Assertions.assertNotNull(doctorRest);
        Assertions.assertNotNull(doctorDTO);
        Assertions.assertNotNull(doctorDTOS);
        Assertions.assertNotNull(doctorAvailabilityDTO);
        Assertions.assertNotNull(spec);
        Assertions.assertNotNull(specializationDTOSet);

    }

    @Test
    void doctorMapperNullValuesGivenTest() {
        DoctorDTO doctorDTO = doctorMapper.map((Doctor) null);
        DoctorAvailabilityDTO doctorAvailabilityDTO = doctorMapper.mapDA(null);
        DoctorRest doctorRest = doctorMapper.map((DoctorRestDTO) null);

        Assertions.assertNull(doctorDTO);
        Assertions.assertNull(doctorAvailabilityDTO);
        Assertions.assertNull(doctorRest);




    }
}