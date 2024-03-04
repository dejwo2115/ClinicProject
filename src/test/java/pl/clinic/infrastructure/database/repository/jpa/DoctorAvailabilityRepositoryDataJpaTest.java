package pl.clinic.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.integration.configuration.PersistenceContainerTestConfiguration;
import pl.clinic.util.EntityFixtures;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorAvailabilityRepositoryDataJpaTest {
    private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
    private DoctorJpaRepository doctorJpaRepository;

    @Test
    void thatFindByDoctorOrderByAvailabilityDateAscStartTimeAscWorksCorrectly() {
        //given
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability1 = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability1.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability2 = EntityFixtures.someDoctorAvailability2();
        doctorAvailability2.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability3 = EntityFixtures.someDoctorAvailability3();
        doctorAvailability3.setDoctor(doctorEntity);
        List<DoctorAvailabilityEntity> toSave = List.of(doctorAvailability1, doctorAvailability2, doctorAvailability3);
        doctorAvailabilityJpaRepository.saveAllAndFlush(toSave);

        //when
        List<DoctorAvailabilityEntity> afterSave = doctorAvailabilityJpaRepository
                .findByDoctorOrderByAvailabilityDateAscStartTimeAsc(doctorEntity);
        //then
        Assertions.assertThat(afterSave.get(0).getAvailabilityDate()).isBefore(afterSave.get(1).getAvailabilityDate());
        Assertions.assertThat(afterSave.get(1).getAvailabilityDate()).isBefore(afterSave.get(2).getAvailabilityDate());
        Assertions.assertThat(afterSave).isNotEmpty();
        Assertions.assertThat(afterSave.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void thatFindStartTimesByDoctorAndDateWorksCorrectly() {
        //given

        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability1 = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability1.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability2 = EntityFixtures.someDoctorAvailability2();
        doctorAvailability2.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability3 = EntityFixtures.someDoctorAvailability3();
        doctorAvailability3.setDoctor(doctorEntity);
        List<DoctorAvailabilityEntity> toSave = List.of(doctorAvailability1, doctorAvailability2, doctorAvailability3);
        doctorAvailabilityJpaRepository.saveAllAndFlush(toSave);
        LocalTime toCheck = doctorAvailability1.getStartTime();
        // when
        List<LocalTime> startTimesByDoctorAndDate = doctorAvailabilityJpaRepository
                .findStartTimesByDoctorAndDate(doctorEntity, doctorAvailability1.getAvailabilityDate());

        Assertions.assertThat(startTimesByDoctorAndDate).isNotEmpty();
        Assertions.assertThat(startTimesByDoctorAndDate.contains(toCheck)).isTrue();
    }

    @Test
    void thatFindByDoctorAndAvailabilityDateAndStartTimeWorksCorrectly() {
        //given
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability1 = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability1.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability2 = EntityFixtures.someDoctorAvailability2();
        doctorAvailability2.setDoctor(doctorEntity);
        DoctorAvailabilityEntity doctorAvailability3 = EntityFixtures.someDoctorAvailability3();
        doctorAvailability3.setDoctor(doctorEntity);
        List<DoctorAvailabilityEntity> toSave = List.of(doctorAvailability1,doctorAvailability2,doctorAvailability3);
        doctorAvailabilityJpaRepository.saveAllAndFlush(toSave);

        Optional<DoctorAvailabilityEntity> first = doctorAvailabilityJpaRepository
                .findByDoctorAndAvailabilityDateAndStartTime(
                        doctorEntity, doctorAvailability1.getAvailabilityDate(), doctorAvailability1.getStartTime());
        Optional<DoctorAvailabilityEntity> second = doctorAvailabilityJpaRepository
                .findByDoctorAndAvailabilityDateAndStartTime(
                        doctorEntity, doctorAvailability2.getAvailabilityDate(), doctorAvailability2.getStartTime());
        Optional<DoctorAvailabilityEntity> third = doctorAvailabilityJpaRepository
                .findByDoctorAndAvailabilityDateAndStartTime(
                        doctorEntity, doctorAvailability3.getAvailabilityDate(), doctorAvailability3.getStartTime());

        Assertions.assertThat(first).isEqualTo(Optional.of(doctorAvailability1));
        Assertions.assertThat(second).isEqualTo(Optional.of(doctorAvailability2));
        Assertions.assertThat(third).isEqualTo(Optional.of(doctorAvailability3));
    }

    @Test
    void shouldThrowWhileIncorrectDataGiven() {
        //given
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        //when, then
        assertThrows(NoSuchElementException.class, () -> {
            doctorAvailabilityJpaRepository.findByDoctorAndAvailabilityDateAndStartTime(doctorEntity, date, time).orElseThrow();
        });
        ;
    }
}