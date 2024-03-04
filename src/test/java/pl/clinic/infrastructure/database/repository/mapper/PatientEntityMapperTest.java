package pl.clinic.infrastructure.database.repository.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.domain.*;
import pl.clinic.infrastructure.database.entity.*;
import pl.clinic.util.EntityFixtures;
import pl.clinic.util.TestClassesFixtures;

import java.util.Set;

class PatientEntityMapperTest {
    private final PatientEntityMapper patientEntityMapper = new PatientEntityMapperImpl();

    @Test
    void mapFromEntity() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        AddressEntity addressEntity = patientEntity.getAddress();
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);
        patientEntity.setAppointments(Set.of(appointmentEntity));

        //when
        Patient patient = patientEntityMapper.mapFromEntity(patientEntity);
        Address address = patientEntityMapper.mapFromEntity(addressEntity);
        Address addressNull = patientEntityMapper.mapFromEntity((AddressEntity) null);
        Patient patientNull = patientEntityMapper.mapFromEntity((PatientEntity) null);
        Prescription prescription = patient.getAppointments().stream().findFirst().get().getPrescription();
        //then

        Assertions.assertThat(prescription).isNotNull();
        Assertions.assertThat(patientNull).isNull();
        Assertions.assertThat(addressNull).isNull();
        Assertions.assertThat(patientEntity.getPatientId()).isEqualTo(patient.getPatientId());
        Assertions.assertThat(patientEntity.getSurname()).isEqualTo(patient.getSurname());
        Assertions.assertThat(patientEntity.getName()).isEqualTo(patient.getName());
        Assertions.assertThat(patientEntity.getBirthDate()).isEqualTo(patient.getBirthDate());
        Assertions.assertThat(patientEntity.getEmail()).isEqualTo(patient.getEmail());
        Assertions.assertThat(patientEntity.getGender()).isEqualTo(patient.getGender());
        Assertions.assertThat(patientEntity.getPhone()).isEqualTo(patient.getPhone());
        Assertions.assertThat(patientEntity.getUserId()).isEqualTo(patient.getUserId());

        Assertions.assertThat(addressEntity.getAddress()).isEqualTo(address.getAddress());
        Assertions.assertThat(addressEntity.getAddressId()).isEqualTo(address.getAddressId());
        Assertions.assertThat(addressEntity.getCity()).isEqualTo(address.getCity());
        Assertions.assertThat(addressEntity.getPostalCode()).isEqualTo(address.getPostalCode());
        Assertions.assertThat(addressEntity.getCountry()).isEqualTo(address.getCountry());
    }
    @Test
    void mapPrescriptionTest() {
        //given
        DoctorAvailabilityEntity doctorAvailability = EntityFixtures.someDoctorAvailabilityWithDoctor();
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailability);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);

        //when
        Prescription prescription = patientEntityMapper.mapFromEntity(prescriptionEntity);
        Prescription prescriptionNull = patientEntityMapper.mapFromEntity((PrescriptionEntity) null);

        //then
        Assertions.assertThat(prescription).isNotNull();
        Assertions.assertThat(prescriptionNull).isNull();

    }
    @Test
    void mapPrescriptionEntityTest() {
        //given
        DoctorAvailability doctorAvailability = TestClassesFixtures.buildDoctorAvailability();
        Appointment appointment = TestClassesFixtures.buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = TestClassesFixtures.buildPrescription(appointment);
        //when
        PrescriptionEntity prescriptionEntity = patientEntityMapper.mapToEntity(prescription);
        PrescriptionEntity prescriptionEntityNull = patientEntityMapper.mapToEntity((Prescription) null);
        //then
        Assertions.assertThat(prescriptionEntity).isNotNull();
        Assertions.assertThat(prescriptionEntityNull).isNull();
    }

    @Test
    void mapToEntity() {
        //given
        Patient patient = TestClassesFixtures.buildPatient();
        Address address = patient.getAddress();
        DoctorAvailability doctorAvailability = TestClassesFixtures.buildDoctorAvailability();
        Appointment appointment = TestClassesFixtures.buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = TestClassesFixtures.buildPrescription(appointment);
        Appointment appointmentWithPrescription = appointment.withPrescription(prescription);

        //when
        PatientEntity patientEntity = patientEntityMapper.mapToEntity(patient.withAppointments(Set.of(appointmentWithPrescription)));
        AddressEntity addressEntity = patientEntityMapper.mapToEntity(address);
        AddressEntity addressEntityNull = patientEntityMapper.mapToEntity((Address) null);
        PatientEntity patientEntityNull = patientEntityMapper.mapToEntity((Patient) null);
        PrescriptionEntity prescription1 = patientEntity.getAppointments().stream().findAny().get().getPrescription();
        //then

        Assertions.assertThat(prescription1).isNotNull();
        Assertions.assertThat(patientEntityNull).isNull();
        Assertions.assertThat(addressEntityNull).isNull();
        Assertions.assertThat(patient.getPatientId()).isEqualTo(patientEntity.getPatientId());
        Assertions.assertThat(patient.getSurname()).isEqualTo(patientEntity.getSurname());
        Assertions.assertThat(patient.getName()).isEqualTo(patientEntity.getName());
        Assertions.assertThat(patient.getBirthDate()).isEqualTo(patientEntity.getBirthDate());
        Assertions.assertThat(patient.getEmail()).isEqualTo(patientEntity.getEmail());
        Assertions.assertThat(patient.getGender()).isEqualTo(patientEntity.getGender());
        Assertions.assertThat(patient.getPhone()).isEqualTo(patientEntity.getPhone());
        Assertions.assertThat(patient.getUserId()).isEqualTo(patientEntity.getUserId());

        Assertions.assertThat(address.getAddress()).isEqualTo(addressEntity.getAddress());
        Assertions.assertThat(address.getAddressId()).isEqualTo(addressEntity.getAddressId());
        Assertions.assertThat(address.getCity()).isEqualTo(addressEntity.getCity());
        Assertions.assertThat(address.getPostalCode()).isEqualTo(addressEntity.getPostalCode());
        Assertions.assertThat(address.getCountry()).isEqualTo(addressEntity.getCountry());


    }
}