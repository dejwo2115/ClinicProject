package pl.clinic.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.AppointmentDAO;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;
import pl.clinic.domain.Prescription;
import pl.clinic.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentService {
    private final AppointmentDAO appointmentDAO;

    @Transactional
    public void makeAppointmentRequest(Patient patient, DoctorAvailability doctorAvailability) {
        appointmentDAO.makeAppointmentRequest(patient, doctorAvailability);
    }

    public List<Appointment> findByPatientEmailAndIsFinishedFalse(String email) {
        return appointmentDAO.findByPatientEmailFinishedFalse(email);
    }

    public Appointment findById(Integer appointmentId) {
        Optional<Appointment> appointment = appointmentDAO.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new NotFoundException("Could not find appointment with id: [%s]".formatted(appointmentId));
        }
        return appointment.get();
    }

    @Transactional
    public void cancelAppointment(Appointment appointment) {
        appointmentDAO.cancelAppointment(appointment);
    }

    public List<Appointment> findByPatientEmailAndIsFinishedTrue(String email) {
        return appointmentDAO.findByPatientEmailAndIsFinishedTrue(email);
    }

    @Transactional
    public void processAppointment(Prescription prescription, Appointment appointment) {
        Appointment toSave = getAppointment(prescription, appointment);
        appointmentDAO.processAppointment(toSave);
    }

    protected Appointment getAppointment(Prescription prescription, Appointment appointment) {
        Prescription prescriptionBuilt = buildPrescription(prescription, appointment);
        return appointment.withPrescription(prescriptionBuilt).withIsFinished(true);
    }

    protected Prescription buildPrescription(Prescription prescription, Appointment appointment) {
        return Prescription.builder()
                .prescribedMedications(prescription.getPrescribedMedications())
                .note(prescription.getNote())
                .expirationDate(appointment.getReservedDate().plusYears(1))
                .issueDate(appointment.getReservedDate())
                .prescriptionCode(generatePrescriptionCode(appointment))
                .build();
    }

    protected String generatePrescriptionCode(Appointment appointment) {
        return "[%s-%s-%s]-[%s-%s]-[%s]".formatted(
                appointment.getPatient().getName(),
                appointment.getPatient().getSurname(),
                appointment.getPatient().getBirthDate().toString(),
                appointment.getReservedDate(),
                appointment.getReservedStartTime(),
                randomInt(10,100));
    }
    @SuppressWarnings("SameParameterValue")
    protected int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
