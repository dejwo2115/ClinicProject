package pl.clinic.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;
import pl.clinic.infrastructure.security.RoleRepository;
import pl.clinic.infrastructure.security.UserEntity;
import pl.clinic.infrastructure.security.UserRepository;
import pl.clinic.integration.configuration.PersistenceContainerTestConfiguration;
import pl.clinic.util.EntityFixtures;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorRepositoryDataJpaTest {
    private DoctorJpaRepository doctorJpaRepository;
    private SpecializationJpaRepository specializationJpaRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Test
    void thatFindAllShouldReturnListOfDoctors() {
        //given
        int beforeSave = doctorJpaRepository.findAll().size();
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        UserEntity userEntity = EntityFixtures.someDoctorUser(doctorEntity);
        userEntity.setRoles(Set.of(roleRepository.findByRole("DOCTOR")));
        UserEntity saved = userRepository.save(userEntity);
        doctorEntity.setUserId(saved.getId());

        //when
        doctorJpaRepository.saveAndFlush(doctorEntity);
        List<DoctorEntity> allDoctors = doctorJpaRepository.findAll();
        //then
        assertThat(allDoctors).isNotNull();
        assertThat(allDoctors).isNotEmpty();
        assertThat(allDoctors.size()).isEqualTo(beforeSave + 1);
        for (DoctorEntity doctor : allDoctors) {
            assertThat(doctor).isInstanceOf(DoctorEntity.class);
        }
    }

    @Test
    void thatFindByIdShouldReturnOptionalOfDoctor() {

        //given,when
        Optional<DoctorEntity> doctorEntity = doctorJpaRepository.findById(1);

        //then
        assertThat(doctorEntity).isNotEmpty();
        assertThat(doctorEntity.get().getDoctorId()).isEqualTo(1);

    }

    @Test
    void thatFindByEmailShouldReturnOptionalOfDoctor() {
        List<DoctorEntity> all = doctorJpaRepository.findAll();
        DoctorEntity doctorEntity = all.get(0);
        String email = doctorEntity.getEmail();
        //when
        Optional<DoctorEntity> foundDoctor = doctorJpaRepository.findByEmail(email);

        //then

        assertThat(foundDoctor).isNotEmpty();
        assertThat(foundDoctor.get()).isEqualTo(doctorEntity);
    }
    @Test
    void thatFindByEmailThrowsExceptionWhenDoctorNotFound() {
        // given
        String nonExistentEmail = "nonexistent@example.com";

        // when, then
        assertThrows(NoSuchElementException.class, () -> {
            doctorJpaRepository.findByEmail(nonExistentEmail).orElseThrow();
        });
    }

    @Test
    void thatFindBySpecializationShouldReturnListWithDoctorsThatHaveGivenSpecializationName() {
        //when
        List<DoctorEntity> doctorListWithSpec = doctorJpaRepository.findBySpecializationName("Logopeda");
        //then
        assertThat(doctorListWithSpec).isNotEmpty();

    }
    @Test
    void shouldReturnEmptyListIfDoctorWithSpecificSpecializationDoseNotExist() {
        //given
        String nonExistingSpec = "nonExistingSpecialization";
        List<DoctorEntity> bySpecializationName = doctorJpaRepository.findBySpecializationName(nonExistingSpec);
        //when, then
        assertThat(bySpecializationName).isEmpty();
    }

    @Test
    void thatSaveDoctorWithUserRoleWorksCorrectly() {
        //given
        int beforeSave = doctorJpaRepository.findAll().size();
        DoctorEntity doctorEntity = EntityFixtures.someDoctor();
        UserEntity userEntity = EntityFixtures.someDoctorUser(doctorEntity);
        userEntity.setRoles(Set.of(roleRepository.findByRole("DOCTOR")));
        UserEntity saved = userRepository.save(userEntity);
        doctorEntity.setUserId(saved.getId());

        //when
        doctorJpaRepository.saveAndFlush(doctorEntity);
        int afterSave = doctorJpaRepository.findAll().size();
        //then
        Assertions.assertThat(afterSave).isEqualTo(beforeSave + 1);
    }

    @Test
    void thatSaveSpecToDoctorShouldCreateNewSpecializationAndAddToDoctor() {
        //given
        SpecializationEntity newSpecialization = SpecializationEntity.builder().name("NewSpecialization").build();
        List<DoctorEntity> all = doctorJpaRepository.findAll();
        DoctorEntity doctorEntity = all.get(0);
        Set<SpecializationEntity> specializations = doctorEntity.getSpecializations();
        int beforeAdding = specializations.size();

        //when
        SpecializationEntity saved = specializationJpaRepository.saveAndFlush(newSpecialization);
        specializations.add(saved);
        doctorJpaRepository.saveAndFlush(doctorEntity);
        int afterAdding = doctorEntity.getSpecializations().size();
        //then
        assertThat(specializations).contains(newSpecialization);
        assertThat(afterAdding).isEqualTo(beforeAdding + 1);
    }
    @Test
    void thatSaveSpecToDoctorShouldIssueExistingSpecialization() {
        //given
        SpecializationEntity onkology = SpecializationEntity.builder().name("Onkology").build();
        SpecializationEntity saved = specializationJpaRepository.saveAndFlush(onkology);
        Optional<SpecializationEntity> existingSpecialization = specializationJpaRepository.findByName(saved.getName());
        List<DoctorEntity> all = doctorJpaRepository.findAll();
        DoctorEntity doctorEntity = all.get(0);
        Set<SpecializationEntity> specializations = doctorEntity.getSpecializations();
        int beforeAdding = specializations.size();

        //when
        specializations.add(existingSpecialization.get());
        doctorJpaRepository.saveAndFlush(doctorEntity);
        int afterAdding = doctorEntity.getSpecializations().size();
        //then
        assertThat(existingSpecialization).isNotEmpty();
        assertThat(specializations).contains(existingSpecialization.get());
        assertThat(afterAdding).isEqualTo(beforeAdding + 1);
    }
}