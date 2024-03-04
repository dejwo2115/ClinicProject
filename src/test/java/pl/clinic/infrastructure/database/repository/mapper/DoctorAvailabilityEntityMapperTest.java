package pl.clinic.infrastructure.database.repository.mapper;

import org.assertj.core.api.Assertions;
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

class DoctorAvailabilityEntityMapperTest {
    private final DoctorAvailabilityEntityMapper doctorAvailabilityEntityMapper = new DoctorAvailabilityEntityMapperImpl();

    @Test
    void mapToEntityDA() {
        //given, when
        DoctorAvailability doctorAvailability = TestClassesFixtures.buildDoctorAvailability();
        DoctorAvailabilityEntity doctorAvailabilityEntity = doctorAvailabilityEntityMapper.mapToEntity(doctorAvailability);
        //then
        Assertions.assertThat(doctorAvailability.getDoctorAvailabilityId()).isEqualTo(doctorAvailabilityEntity.getDoctorAvailabilityId());
        Assertions.assertThat(doctorAvailability.getAvailabilityDate()).isEqualTo(doctorAvailabilityEntity.getAvailabilityDate());
        Assertions.assertThat(doctorAvailability.getReserved()).isEqualTo(doctorAvailabilityEntity.getReserved());
        Assertions.assertThat(doctorAvailability.getStartTime()).isEqualTo(doctorAvailabilityEntity.getStartTime());
    }

    @Test
    void mapFromEntityDA() {
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();

        DoctorAvailability doctorAvailability = doctorAvailabilityEntityMapper.mapFromEntity(doctorAvailabilityEntity);

        Assertions.assertThat(doctorAvailabilityEntity.getDoctorAvailabilityId()).isEqualTo(doctorAvailability.getDoctorAvailabilityId());
        Assertions.assertThat(doctorAvailabilityEntity.getAvailabilityDate()).isEqualTo(doctorAvailability.getAvailabilityDate());
        Assertions.assertThat(doctorAvailabilityEntity.getReserved()).isEqualTo(doctorAvailability.getReserved());
        Assertions.assertThat(doctorAvailabilityEntity.getStartTime()).isEqualTo(doctorAvailability.getStartTime());
    }

    @Test
    void mapFromEntityDoctor() {
        //given
        Set<SpecializationEntity> someSpecs = Set.of(SpecializationEntity.builder().specializationId(1).name("someSpec").build());
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        doctorEntity.setSpecializations(someSpecs);
        //when
        Doctor doctor = doctorAvailabilityEntityMapper.mapFromEntity(doctorEntity);
        //then
        Assertions.assertThat(doctorEntity.getDoctorId()).isEqualTo(doctor.getDoctorId());
        Assertions.assertThat(doctorEntity.getName()).isEqualTo(doctor.getName());
        Assertions.assertThat(doctorEntity.getSurname()).isEqualTo(doctor.getSurname());
        Assertions.assertThat(doctorEntity.getBirthDate()).isEqualTo(doctor.getBirthDate());
        Assertions.assertThat(doctorEntity.getGender()).isEqualTo(doctor.getGender());
        Assertions.assertThat(doctorEntity.getUserId()).isEqualTo(doctor.getUserId());
        Assertions.assertThat(doctorEntity.getSpecializations().size()).isEqualTo(doctor.getSpecializations().size());

    }
    @Test
    void mapFromEntityWithNullShouldReturnNull() {
        //given, when
        Doctor doctorNull = doctorAvailabilityEntityMapper.mapFromEntity((DoctorEntity) null);
        DoctorAvailability doctorAvailabilityNull = doctorAvailabilityEntityMapper.mapFromEntity((DoctorAvailabilityEntity) null);
        //then
        Assertions.assertThat(doctorNull).isNull();
        Assertions.assertThat(doctorAvailabilityNull).isNull();
    }
    @Test
    void mapToEntityWithNullShouldReturnNull() {
        //given
        DoctorEntity doctorNull = doctorAvailabilityEntityMapper.mapToEntity((Doctor) null);
        DoctorAvailabilityEntity doctorAvailabilityEntityNull = doctorAvailabilityEntityMapper.mapToEntity((DoctorAvailability) null);
        //when, then
        Assertions.assertThat(doctorNull).isNull();
        Assertions.assertThat(doctorAvailabilityEntityNull).isNull();
    }

    @Test
    void mapToEntityDoctor() {
        //given
        Doctor doctor = TestClassesFixtures.buildDoctor()
                .withSpecializations(Set.of(Specialization.builder().specializationId(1).name("someSpec").build()));
        //when
        DoctorEntity doctorEntity = doctorAvailabilityEntityMapper.mapToEntity(doctor);
        //then
        Assertions.assertThat(doctor.getDoctorId()).isEqualTo(doctorEntity.getDoctorId());
        Assertions.assertThat(doctor.getName()).isEqualTo(doctorEntity.getName());
        Assertions.assertThat(doctor.getSurname()).isEqualTo(doctorEntity.getSurname());
        Assertions.assertThat(doctor.getBirthDate()).isEqualTo(doctorEntity.getBirthDate());
        Assertions.assertThat(doctor.getGender()).isEqualTo(doctorEntity.getGender());
        Assertions.assertThat(doctor.getUserId()).isEqualTo(doctorEntity.getUserId());
        Assertions.assertThat(doctor.getSpecializations().size()).isEqualTo(doctorEntity.getSpecializations().size());
    }
}