package pl.clinic.business.dao;

import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Patient;

import java.util.List;
import java.util.Optional;

public interface AppointmentDAO {
    void makeAppointmentRequest(Patient patient, DoctorAvailability doctorAvailability);
    void processAppointment(Appointment toSave);
    List<Appointment> findByPatientEmailFinishedFalse(String email);
    Optional<Appointment> findById(Integer appointmentId);
    void cancelAppointment(Appointment appointment);
    List<Appointment> findByPatientEmailAndIsFinishedTrue(String email);

}
