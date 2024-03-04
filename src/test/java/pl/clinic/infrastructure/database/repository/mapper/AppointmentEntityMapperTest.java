package pl.clinic.infrastructure.database.repository.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;
import pl.clinic.util.EntityFixtures;
import pl.clinic.util.TestClassesFixtures;

class AppointmentEntityMapperTest {
    private final AppointmentEntityMapper appointmentEntityMapper = new AppointmentEntityMapperImpl();

    @Test
    void mapToEntityAppointmentTest() {
        //given

        Patient patient = TestClassesFixtures.buildPatient();
        DoctorAvailability doctorAvailability = TestClassesFixtures.buildDoctorAvailability();
        Appointment appointment = TestClassesFixtures.buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = TestClassesFixtures.buildPrescription(appointment);
        Appointment appointmentToMap = appointment.withPrescription(prescription).withPatient(patient);

        //when
        AppointmentEntity appointmentEntity = appointmentEntityMapper.mapToEntity(appointmentToMap);
        AppointmentEntity appointmentEntityNull = appointmentEntityMapper.mapToEntity((Appointment) null);

        //then
        Assertions.assertThat(appointmentEntity).isNotNull();
        Assertions.assertThat(appointmentEntity.getDoctorAvailability().getDoctor()).isNotNull();
        Assertions.assertThat(appointmentEntity.getDoctorAvailability().getDoctor().getSpecializations()).isNotNull();
        Assertions.assertThat(appointmentEntityNull).isNull();


    }

    @Test
    void mapFromEntityAppointmentTest() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailabilityEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);
        appointmentEntity.setPatient(patientEntity);

        //when
        Appointment appointment = appointmentEntityMapper.mapFromEntity(appointmentEntity);
        Appointment appointmentNull = appointmentEntityMapper.mapFromEntity((AppointmentEntity) null);

        //then
        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(appointment.getDoctorAvailability().getDoctor()).isNotNull();
        Assertions.assertThat(appointment.getDoctorAvailability().getDoctor().getSpecializations()).isNotNull();
        Assertions.assertThat(appointmentNull).isNull();

    }
    @Test
    void mapNullEntitiesWillReturnNull() {
        //given, when
        PatientEntity patientEntity = appointmentEntityMapper.mapToEntity((Patient)null);
        DoctorAvailabilityEntity doctorAvailabilityEntity = appointmentEntityMapper.mapToEntity((DoctorAvailability) null);
        PrescriptionEntity prescriptionEntity = appointmentEntityMapper.mapToEntity((Prescription) null);
        Patient patient = appointmentEntityMapper.mapFromEntity((PatientEntity) null);
        DoctorAvailability doctorAvailability = appointmentEntityMapper.mapFromEntity((DoctorAvailabilityEntity) null);
        Prescription prescription = appointmentEntityMapper.mapFromEntity((PrescriptionEntity) null);

        //then
        Assertions.assertThat(patientEntity).isNull();
        Assertions.assertThat(doctorAvailabilityEntity).isNull();
        Assertions.assertThat(prescriptionEntity).isNull();
        Assertions.assertThat(patient).isNull();
        Assertions.assertThat(doctorAvailability).isNull();
        Assertions.assertThat(prescription).isNull();

    }
}