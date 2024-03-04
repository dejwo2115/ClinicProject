package pl.clinic.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.domain.exception.ProcessingException;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;
import pl.clinic.infrastructure.database.repository.jpa.AppointmentJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.DoctorAvailabilityJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.AppointmentEntityMapper;
import pl.clinic.infrastructure.database.repository.mapper.PrescriptionEntityMapper;
import pl.clinic.util.EntityFixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.clinic.util.TestClassesFixtures.*;

@ExtendWith(MockitoExtension.class)
class AppointmentRepositoryTest {
    @InjectMocks
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentJpaRepository appointmentJpaRepository;
    @Mock
    private AppointmentEntityMapper appointmentEntityMapper;
    @Mock
    private PatientJpaRepository patientJpaRepository;
    @Mock
    private PrescriptionEntityMapper prescriptionEntityMapper;
    @Mock
    private DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
    @Test
    void thatMakeAppointmentRequestShouldThrowIfAvailabilityIsReserved() {
        // given
        Patient patient = buildPatient().withEmail("patient@example.com");
        DoctorAvailability doctorAvailability = buildDoctorAvailability().withDoctorAvailabilityId(1).withReserved(true);
        PatientEntity patientEntity = EntityFixtures.somePatient();
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailability2();
        doctorAvailabilityEntity.setReserved(true);

        when(patientJpaRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patientEntity));
        when(doctorAvailabilityJpaRepository.findById(doctorAvailability.getDoctorAvailabilityId())).thenReturn(Optional.of(doctorAvailabilityEntity));

        // when
        ProcessingException exception = assertThrows(ProcessingException.class, () -> appointmentRepository.makeAppointmentRequest(patient, doctorAvailability));

        // then
        verify(patientJpaRepository).findByEmail(patient.getEmail());
        verify(doctorAvailabilityJpaRepository).findById(doctorAvailability.getDoctorAvailabilityId());
        verify(appointmentJpaRepository, never()).findByPatientEmailAndIsFinishedFalse(patient.getEmail());
        verify(doctorAvailabilityJpaRepository, never()).saveAndFlush(any());
        verify(appointmentJpaRepository, never()).saveAndFlush(any());

        String expectedMessage = "Sorry but actually someone has made reservation appointment for that specific time, please choose another time to make reservation.";
        assert(exception.getMessage().contains(expectedMessage));
    }
    @Test
    void thatMakeAppointmentShouldThrowWhenPatientDefinedAppointmentForSpecificDateAndTime() {
        // given
        Patient patient = buildPatient().withEmail("patient@example.com");
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        PatientEntity patientEntity = EntityFixtures.somePatient();
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();


        when(patientJpaRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patientEntity));
        when(doctorAvailabilityJpaRepository.findById(doctorAvailability.getDoctorAvailabilityId())).thenReturn(Optional.of(doctorAvailabilityEntity));

        List<AppointmentEntity> notFinishedAppointments = new ArrayList<>();
        AppointmentEntity appointment = EntityFixtures.someAppointment(doctorAvailabilityEntity);
        appointment.setPatient(patientEntity);
        notFinishedAppointments.add(appointment);
        when(appointmentJpaRepository.findByPatientEmailAndIsFinishedFalse(patient.getEmail())).thenReturn(notFinishedAppointments);

        // when
        ProcessingException exception = assertThrows(ProcessingException.class, () -> appointmentRepository.makeAppointmentRequest(patient, doctorAvailability));

        // then
        verify(patientJpaRepository).findByEmail(patient.getEmail());
        verify(doctorAvailabilityJpaRepository).findById(doctorAvailability.getDoctorAvailabilityId());
        verify(appointmentJpaRepository).findByPatientEmailAndIsFinishedFalse(patient.getEmail());
        verify(doctorAvailabilityJpaRepository, never()).saveAndFlush(any());
        verify(appointmentJpaRepository, never()).saveAndFlush(any());

        String expectedMessage = "You have appointment for current date: [%s] and time: [%s]".formatted(doctorAvailability.getAvailabilityDate(), doctorAvailability.getStartTime());
        assert(exception.getMessage().contains(expectedMessage));
    }
    @Test
    void thatMakeAppointmentShouldSaveCorrectly() {
        // given
        Patient patient = buildPatient().withEmail("patient@example.com");
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        PatientEntity patientEntity = EntityFixtures.somePatient();
        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();


        when(patientJpaRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patientEntity));
        when(doctorAvailabilityJpaRepository.findById(doctorAvailability.getDoctorAvailabilityId())).thenReturn(Optional.of(doctorAvailabilityEntity));

        List<AppointmentEntity> notFinishedAppointments = new ArrayList<>();
        when(appointmentJpaRepository.findByPatientEmailAndIsFinishedFalse(patient.getEmail())).thenReturn(notFinishedAppointments);

        //when
        appointmentRepository.makeAppointmentRequest(patient, doctorAvailability);

        //then

        verify(doctorAvailabilityJpaRepository).saveAndFlush(doctorAvailabilityEntity);
        verify(appointmentJpaRepository).saveAndFlush(any(AppointmentEntity.class));
    }

    @Test
    void processAppointment() {
        //given
        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = buildPrescription(appointment);
        Appointment appointmentToTransfer = appointment.withPrescription(prescription);

        DoctorAvailabilityEntity doctorAvailabilityEntity = EntityFixtures.someDoctorAvailabilityWithDoctor();
        AppointmentEntity appointmentEntity = EntityFixtures.someAppointment(doctorAvailabilityEntity);
        PrescriptionEntity prescriptionEntity = EntityFixtures.somePrescription(appointmentEntity);
        appointmentEntity.setPrescription(prescriptionEntity);

        when(appointmentJpaRepository.findById(appointmentToTransfer.getAppointmentId())).thenReturn(Optional.of(appointmentEntity));
        when(prescriptionEntityMapper.mapToEntity(appointmentToTransfer.getPrescription())).thenReturn(prescriptionEntity);

        // when
        appointmentRepository.processAppointment(appointmentToTransfer);

        // then
        verify(appointmentJpaRepository).findById(appointmentToTransfer.getAppointmentId());
        verify(prescriptionEntityMapper).mapToEntity(appointmentToTransfer.getPrescription());
        verify(appointmentJpaRepository).saveAndFlush(appointmentEntity);
    }

    @Test
    void findByPatientEmailFinishedFalseShouldReturnListWithAppointments() {
        //given
        Patient patient = buildPatient();
        AppointmentEntity appointmentEntity1 = AppointmentEntity.builder().isFinished(false).build();
        AppointmentEntity appointmentEntity2 = AppointmentEntity.builder().isFinished(false).build();
        when(appointmentJpaRepository.findByPatientEmailAndIsFinishedFalse(patient.getEmail()))
                .thenReturn(List.of(appointmentEntity1, appointmentEntity2));

        Appointment appointment1 = Appointment.builder().isFinished(false).build();
        Appointment appointment2 = Appointment.builder().isFinished(false).build();

        when(appointmentEntityMapper.mapFromEntity(appointmentEntity1)).thenReturn(appointment1);
        when(appointmentEntityMapper.mapFromEntity(appointmentEntity2)).thenReturn(appointment2);

        //when
        List<Appointment> appointments = appointmentRepository.findByPatientEmailFinishedFalse(patient.getEmail());

        // then
        assertEquals(2, appointments.size());
        assertEquals(appointment1, appointments.get(0));
        assertEquals(appointment2, appointments.get(1));
        assertFalse(appointment1.getIsFinished());
        assertFalse(appointment2.getIsFinished());

        verify(appointmentJpaRepository).findByPatientEmailAndIsFinishedFalse(patient.getEmail());

        verify(appointmentEntityMapper, times(2)).mapFromEntity(any(AppointmentEntity.class));
    }

    @Test
    void thatFindByIdShouldReturnNotEmptyValue() {
        // given
        Integer appointmentId = 1;
        AppointmentEntity appointmentEntity = AppointmentEntity.builder().build();
        when(appointmentJpaRepository.findById(appointmentId)).thenReturn(Optional.of(appointmentEntity));

        Appointment appointment = Appointment.builder().build();
        when(appointmentEntityMapper.mapFromEntity(appointmentEntity)).thenReturn(appointment);

        // when
        Optional<Appointment> result = appointmentRepository.findById(appointmentId);

        // then
        assertTrue(result.isPresent());
        assertEquals(appointment, result.get());

        verify(appointmentJpaRepository).findById(appointmentId);
        verify(appointmentEntityMapper, times(1)).mapFromEntity(appointmentEntity);
    }

    @Test
    void thatFindByEmailShouldReturnOptionalEmptyIfNotFound() {
        // given
        Integer appointmentId = 1;
        when(appointmentJpaRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // when
        Optional<Appointment> result = appointmentRepository.findById(appointmentId);

        // then
        assertTrue(result.isEmpty());

        verify(appointmentJpaRepository).findById(appointmentId);
        verifyNoInteractions(appointmentEntityMapper);
    }
    @Test
    void cancelAppointment() {
        // given
        Appointment appointment = Appointment.builder().appointmentId(1).build();

        // when
        appointmentRepository.cancelAppointment(appointment);

        // then
        verify(appointmentJpaRepository).deleteQuery(appointment.getAppointmentId());
    }

    @Test
    void findByPatientEmailAndIsFinishedTrue() {
        //given
        Patient patient = buildPatient();
        AppointmentEntity appointmentEntity1 = AppointmentEntity.builder().isFinished(true).build();
        AppointmentEntity appointmentEntity2 = AppointmentEntity.builder().isFinished(true).build();
        when(appointmentJpaRepository.findByPatientEmailAndIsFinishedTrue(patient.getEmail()))
                .thenReturn(List.of(appointmentEntity1, appointmentEntity2));

        Appointment appointment1 = Appointment.builder().isFinished(true).build();
        Appointment appointment2 = Appointment.builder().isFinished(true).build();

        when(appointmentEntityMapper.mapFromEntity(appointmentEntity1)).thenReturn(appointment1);
        when(appointmentEntityMapper.mapFromEntity(appointmentEntity2)).thenReturn(appointment2);

        //when
        List<Appointment> appointments = appointmentRepository.findByPatientEmailAndIsFinishedTrue(patient.getEmail());

        // then
        assertEquals(2, appointments.size());
        assertEquals(appointment1, appointments.get(0));
        assertEquals(appointment2, appointments.get(1));
        assertTrue(appointment1.getIsFinished());
        assertTrue(appointment2.getIsFinished());

        verify(appointmentJpaRepository).findByPatientEmailAndIsFinishedTrue(patient.getEmail());

        verify(appointmentEntityMapper, times(2)).mapFromEntity(any(AppointmentEntity.class));
    }
}