package pl.clinic.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.AppointmentDAO;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
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

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AppointmentRepository implements AppointmentDAO {
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final AppointmentEntityMapper appointmentEntityMapper;
    private final PatientJpaRepository patientJpaRepository;
    private final PrescriptionEntityMapper prescriptionEntityMapper;
    private final DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;

    @Override
    @Transactional
    public void makeAppointmentRequest(Patient patient, DoctorAvailability doctorAvailability) {
        PatientEntity patientEntity = patientJpaRepository.findByEmail(patient.getEmail()).orElseThrow();
        DoctorAvailabilityEntity doctorAvailabilityEntity = doctorAvailabilityJpaRepository.findById(doctorAvailability.getDoctorAvailabilityId()).orElseThrow();
        if (doctorAvailabilityEntity.getReserved()) {
            throw new ProcessingException(
                    "Sorry but actually someone has made reservation appointment for that specific time, please choose another time to make reservation.");
        }
        List<AppointmentEntity> notFinishedAppointments = appointmentJpaRepository.findByPatientEmailAndIsFinishedFalse(patient.getEmail());
        List<AppointmentEntity> appointmentEntities = notFinishedAppointments.stream()
                .filter(x -> x.getReservedDate().equals(doctorAvailability.getAvailabilityDate()))
                .filter(y -> y.getReservedStartTime().equals(doctorAvailability.getStartTime()))
                .filter(z -> !z.getCanceled())
                .toList();
        if (appointmentEntities.size() >= 1) {
            throw new ProcessingException("You have appointment for current date: [%s] and time: [%s]"
                    .formatted(doctorAvailability.getAvailabilityDate(), doctorAvailability.getStartTime()));
        } else {
            doctorAvailabilityEntity.setReserved(true);
            doctorAvailabilityJpaRepository.saveAndFlush(doctorAvailabilityEntity);
            AppointmentEntity toSave = AppointmentEntity.builder()
                    .reservedStartTime(doctorAvailability.getStartTime())
                    .reservedDate(doctorAvailability.getAvailabilityDate())
                    .doctorAvailability(doctorAvailabilityEntity)
                    .patient(patientEntity)
                    .isFinished(false)
                    .canceled(false)
                    .build();
            appointmentJpaRepository.saveAndFlush(toSave);
        }
    }

    @Override
    @Transactional
    public void processAppointment(Appointment appointment) {
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findById(appointment.getAppointmentId()).orElseThrow();
        PrescriptionEntity prescriptionEntity = prescriptionEntityMapper.mapToEntity(appointment.getPrescription());
        appointmentEntity.setPrescription(prescriptionEntity);
        appointmentJpaRepository.saveAndFlush(appointmentEntity);
    }

    @Override
    public List<Appointment> findByPatientEmailFinishedFalse(String email) {
        return appointmentJpaRepository.findByPatientEmailAndIsFinishedFalse(email)
                .stream().map(appointmentEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Optional<Appointment> findById(Integer appointmentId) {
        return appointmentJpaRepository.findById(appointmentId)
                .map(appointmentEntityMapper::mapFromEntity);
    }

    @Transactional
    @Override
    public void cancelAppointment(Appointment appointment) {
        Integer appointmentId = appointment.getAppointmentId();
        appointmentJpaRepository.deleteQuery(appointmentId);
    }

    @Override
    public List<Appointment> findByPatientEmailAndIsFinishedTrue(String email) {
        return appointmentJpaRepository.findByPatientEmailAndIsFinishedTrue(email).stream()
                .map(appointmentEntityMapper::mapFromEntity)
                .toList();
    }

}
