package pl.clinic.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.DoctorAvailabilityDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.exception.NotFoundException;
import pl.clinic.domain.exception.ProcessingException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class DoctorAvailabilityService {
    private final DoctorAvailabilityDAO doctorAvailabilityDAO;

    @Transactional
    public void createAvailabilityForDay(LocalDate date, LocalTime startTime, Integer amountOfVisits, Doctor doctor) {
        if (date.isBefore(LocalDate.now())) {
            throw new ProcessingException("Given date is from past: [%s]. Current: [%s]. Insert correct date.".formatted(date, LocalDate.now()));
        } else {
            List<DoctorAvailability> availability = IntStream.range(0, amountOfVisits)
                    .mapToObj(x -> {
                        LocalTime visitStartTime = startTime.plusMinutes(x * 30L);
                        return DoctorAvailability.builder()
                                .availabilityDate(date)
                                .doctor(doctor)
                                .reserved(false)
                                .startTime(visitStartTime)
                                .build();
                    })
                    .toList();
            doctorAvailabilityDAO.createAvailabilityForDay(doctor, availability);
        }
    }
    @Transactional
    public DoctorAvailability findById(Integer id) {
        Optional<DoctorAvailability> doctorAvailability = doctorAvailabilityDAO.findById(id);
        if (doctorAvailability.isEmpty()) {
            throw new NotFoundException("Could not find availability with id: [%s]".formatted(id));
        }
        return doctorAvailability.get();
    }
    @Transactional
    public List<DoctorAvailability> findByDoctor(Doctor doctor) {
        return doctorAvailabilityDAO.findByDoctor(doctor);
    }

    public List<LocalTime> findTimesForDate(Doctor doctor, LocalDate date) {
        return doctorAvailabilityDAO.findTimesForDate(doctor, date);
    }
    public DoctorAvailability findByDoctorAndDateAndTime(Doctor doctor, LocalDate selectedDate, LocalTime selectedTime) {
        Optional<DoctorAvailability> doctorAvailability = doctorAvailabilityDAO.findByDoctorAndDateAndTime(doctor, selectedDate, selectedTime);
        if (doctorAvailability.isEmpty()) {
            throw new NotFoundException("Could not find availability with for selected date: [%s], time:[%s]".formatted(selectedDate, selectedTime));
        }
        return doctorAvailability.get();
    }
}
