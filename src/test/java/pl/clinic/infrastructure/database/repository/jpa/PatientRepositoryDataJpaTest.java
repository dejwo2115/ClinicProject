package pl.clinic.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.security.RoleRepository;
import pl.clinic.infrastructure.security.UserEntity;
import pl.clinic.infrastructure.security.UserRepository;
import pl.clinic.integration.configuration.PersistenceContainerTestConfiguration;
import pl.clinic.util.EntityFixtures;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientRepositoryDataJpaTest {
    private PatientJpaRepository patientJpaRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Test
    void thatSavePatientWithUserRoleWorksCorrectly() {
        //given
        int beforeSave = patientJpaRepository.findAll().size();
        PatientEntity patientEntity = EntityFixtures.somePatient();
        UserEntity userEntity = EntityFixtures.somePatientUser(patientEntity);
        userEntity.setRoles(Set.of(roleRepository.findByRole("PATIENT")));
        UserEntity saved = userRepository.save(userEntity);
        patientEntity.setUserId(saved.getId());

        //when
        patientJpaRepository.saveAndFlush(patientEntity);
        int afterSave = patientJpaRepository.findAll().size();
        //then
        assertThat(afterSave).isEqualTo(beforeSave + 1);
    }

    @Test
    void thatFindByEmailWorksCorrectly() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        UserEntity userEntity = EntityFixtures.somePatientUser(patientEntity);
        userEntity.setRoles(Set.of(roleRepository.findByRole("PATIENT")));
        UserEntity saved = userRepository.save(userEntity);
        patientEntity.setUserId(saved.getId());
        //when
        patientJpaRepository.saveAndFlush(patientEntity);
        PatientEntity byEmail = patientJpaRepository.findByEmail(patientEntity.getEmail()).orElseThrow();

        //then
        assertThat(byEmail).isEqualTo(patientEntity);
    }

    @Test
    void thatFindAllWorksCorrectly() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        UserEntity userEntity = EntityFixtures.somePatientUser(patientEntity);
        userEntity.setRoles(Set.of(roleRepository.findByRole("PATIENT")));
        UserEntity saved = userRepository.save(userEntity);
        patientEntity.setUserId(saved.getId());
        //when
        List<PatientEntity> all = patientJpaRepository.findAll();
        //then

        assertThat(all).isNotEmpty();
        for (PatientEntity patient : all) {
            System.out.println(patient.getAddress());
            assertThat(patient).isInstanceOf(PatientEntity.class);
        }

    }
}