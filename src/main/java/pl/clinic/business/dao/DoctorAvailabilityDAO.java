package pl.clinic.business.dao;

import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DoctorAvailabilityDAO {
    void createAvailabilityForDay(Doctor doctor, List<DoctorAvailability> doctorAvailability);
    Optional<DoctorAvailability> findById(Integer id);
    List<DoctorAvailability> findByDoctor(Doctor doctor);
    List<LocalTime> findTimesForDate(Doctor doctor, LocalDate date);
    Optional<DoctorAvailability> findByDoctorAndDateAndTime(Doctor doctor, LocalDate selectedDate, LocalTime selectedTime);
}
