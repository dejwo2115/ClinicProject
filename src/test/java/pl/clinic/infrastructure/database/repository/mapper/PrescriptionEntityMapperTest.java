package pl.clinic.infrastructure.database.repository.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Prescription;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;
import pl.clinic.util.EntityFixtures;
import pl.clinic.util.TestClassesFixtures;

class PrescriptionEntityMapperTest {
    private final PrescriptionEntityMapper prescriptionEntityMapper = new PrescriptionEntityMapperImpl();

    @Test
    void mapToEntityTest() {
        //given
        DoctorAvailability doctorAvailability = TestClassesFixtures.buildDoctorAvailability();
        Appointment appointment = TestClassesFixtures.buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = TestClassesFixtures.buildPrescription(appointment);

        //when
        PrescriptionEntity prescriptionEntity = prescriptionEntityMapper.mapToEntity(prescription);
        PrescriptionEntity prescriptionEntityNull = prescriptionEntityMapper.mapToEntity(null);

        //then
        Assertions.assertThat(prescriptionEntity).isNotNull();
        Assertions.assertThat(prescriptionEntityNull).isNull();
    }

    @Test
    void mapFromEntityTest() {
        //given
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailabilityEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);

        //when
        Prescription prescription = prescriptionEntityMapper.mapFromEntity(prescriptionEntity);
        Prescription prescriptionNull = prescriptionEntityMapper.mapFromEntity(null);
        //then

        Assertions.assertThat(prescription).isNotNull();
        Assertions.assertThat(prescriptionNull).isNull();
    }
}