package pl.clinic.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.clinic.infrastructure.database.entity.*;
import pl.clinic.integration.configuration.PersistenceContainerTestConfiguration;
import pl.clinic.util.EntityFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AppointmentJpaRepositoryDataJpaTest {
    private PrescriptionJpaRepository prescriptionJpaRepository;
    private AppointmentJpaRepository appointmentJpaRepository;
    private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
    private DoctorJpaRepository doctorJpaRepository;
    private PatientJpaRepository patientJpaRepository;

    @Test
    void thatSavingAppointmentWorksCorrectly() {
        //given
        PatientEntity patientEntity = patientJpaRepository.findAll().get(0);
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability.setDoctor(doctorEntity);
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        appointmentEntity.setPatient(patientEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);
        //when
        doctorAvailability.setReserved(true);
        doctorAvailabilityJpaRepository.saveAndFlush(doctorAvailability);
        prescriptionJpaRepository.saveAndFlush(prescriptionEntity);
        appointmentEntity.setIsFinished(true);
        AppointmentEntity saved = appointmentJpaRepository.saveAndFlush(appointmentEntity);
        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getPrescription()).isNotNull();
        assertThat(saved.getPatient()).isNotNull();
        assertThat(saved.getIsFinished()).isTrue();
        assertThat(saved.getDoctorAvailability()).isNotNull();
        assertThat(saved.getDoctorAvailability().getDoctor()).isNotNull();

    }
    @Test
    void findByPatientEmailAndIsFinishedFalse() {
        //given
        PatientEntity patientEntity = patientJpaRepository.findAll().get(0);
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability.setDoctor(doctorEntity);
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        appointmentEntity.setPatient(patientEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);
        //when
        doctorAvailabilityJpaRepository.saveAndFlush(doctorAvailability);
        AppointmentEntity saved = appointmentJpaRepository.saveAndFlush(appointmentEntity);
        appointmentJpaRepository.findById(saved.getAppointmentId());
        //then
        assertThat(saved).isNotNull();


    }

    @Test
    void findByPatientEmailAndIsFinishedTrue() {
        //given
        PatientEntity patientEntity = patientJpaRepository.findAll().get(0);
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability.setDoctor(doctorEntity);
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        appointmentEntity.setPatient(patientEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);

        doctorAvailability.setReserved(true);
        doctorAvailabilityJpaRepository.saveAndFlush(doctorAvailability);
        prescriptionJpaRepository.saveAndFlush(prescriptionEntity);
        appointmentEntity.setIsFinished(true);
        appointmentJpaRepository.saveAndFlush(appointmentEntity);

        //when
        List<AppointmentEntity> byPatientEmailAndIsFinishedTrue = appointmentJpaRepository.findByPatientEmailAndIsFinishedTrue(patientEntity.getEmail());
        //then
        assertThat(byPatientEmailAndIsFinishedTrue.get(0)).isNotNull();
        assertThat(byPatientEmailAndIsFinishedTrue.get(0).getIsFinished()).isTrue();
        assertThat(byPatientEmailAndIsFinishedTrue).isNotEmpty();
    }

    @Test
    void thatDeleteQueryShouldDeleteNotProceededAppointment() {
        //given
        PatientEntity patientEntity = patientJpaRepository.findAll().get(0);
        DoctorEntity doctorEntity = doctorJpaRepository.findAll().get(0);
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        doctorAvailability.setDoctor(doctorEntity);
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        appointmentEntity.setPatient(patientEntity);

        doctorAvailabilityJpaRepository.saveAndFlush(doctorAvailability);
        appointmentJpaRepository.saveAndFlush(appointmentEntity);
        //when
        appointmentJpaRepository.delete(appointmentEntity);
        Optional<AppointmentEntity> byId = appointmentJpaRepository.findById(appointmentEntity.getAppointmentId());
        //then
        assertThat(byId).isEmpty();


    }
}