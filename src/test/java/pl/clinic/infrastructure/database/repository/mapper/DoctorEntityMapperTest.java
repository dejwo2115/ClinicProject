package pl.clinic.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;
import pl.clinic.util.EntityFixtures;
import pl.clinic.util.TestClassesFixtures;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorEntityMapperTest {
    private final DoctorEntityMapper doctorEntityMapper = new DoctorEntityMapperImpl();

    @Test
    void mapFromEntity() {
        //given
        Set<SpecializationEntity> someSpecs = Set.of(SpecializationEntity.builder().specializationId(1).name("someSpec").build());
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        doctorEntity.setSpecializations(someSpecs);
        //when
        Doctor doctor = doctorEntityMapper.mapFromEntity(doctorEntity);
        //then
        assertThat(doctorEntity.getDoctorId()).isEqualTo(doctor.getDoctorId());
        assertThat(doctorEntity.getName()).isEqualTo(doctor.getName());
        assertThat(doctorEntity.getSurname()).isEqualTo(doctor.getSurname());
        assertThat(doctorEntity.getBirthDate()).isEqualTo(doctor.getBirthDate());
        assertThat(doctorEntity.getGender()).isEqualTo(doctor.getGender());
        assertThat(doctorEntity.getUserId()).isEqualTo(doctor.getUserId());
        assertThat(doctorEntity.getSpecializations().size()).isEqualTo(doctor.getSpecializations().size());

    }
    @Test
    void mapFromEntityWithNullShouldReturnNull() {
        //given, when
        Doctor doctorNull = doctorEntityMapper.mapFromEntity((DoctorEntity) null);
        //then
        assertThat(doctorNull).isNull();
    }
    @Test
    void mapToEntityWithNullShouldReturnNull() {
        //given, when
        DoctorEntity doctorNull = doctorEntityMapper.mapToEntity((Doctor) null);
        //then
        assertThat(doctorNull).isNull();
    }

    @Test
    void mapToEntity() {
        //given
        Doctor doctor = TestClassesFixtures.buildDoctor()
                .withSpecializations(Set.of(Specialization.builder().specializationId(1).name("someSpec").build()));
        //when
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        //then
        assertThat(doctor.getDoctorId()).isEqualTo(doctorEntity.getDoctorId());
        assertThat(doctor.getName()).isEqualTo(doctorEntity.getName());
        assertThat(doctor.getSurname()).isEqualTo(doctorEntity.getSurname());
        assertThat(doctor.getBirthDate()).isEqualTo(doctorEntity.getBirthDate());
        assertThat(doctor.getGender()).isEqualTo(doctorEntity.getGender());
        assertThat(doctor.getUserId()).isEqualTo(doctorEntity.getUserId());
        assertThat(doctor.getSpecializations().size()).isEqualTo(doctorEntity.getSpecializations().size());
    }
    @Test
    void mapDoctorAvailability() {
        //given
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailability2();
        DoctorAvailability doctorAvailability1 = TestClassesFixtures.buildDoctorAvailability();

        //when
        DoctorAvailabilityEntity doctorAvailabilityEntityNull = doctorEntityMapper.mapToEntity((DoctorAvailability) null);
        DoctorAvailability doctorAvailabilityNull = doctorEntityMapper.mapFromEntity((DoctorAvailabilityEntity) null);
        DoctorAvailabilityEntity doctorAvailabilityEntity1 = doctorEntityMapper.mapToEntity(doctorAvailability1);
        DoctorAvailability doctorAvailability = doctorEntityMapper.mapFromEntity(doctorAvailabilityEntity);

        //then
        assertThat(doctorAvailability).isNotNull();
        assertThat(doctorAvailabilityEntity1).isNotNull();
        assertThat(doctorAvailabilityNull).isNull();
        assertThat(doctorAvailabilityEntityNull).isNull();

    }
}