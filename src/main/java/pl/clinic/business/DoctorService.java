package pl.clinic.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.DoctorDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;
import pl.clinic.domain.exception.NotFoundException;
import pl.clinic.domain.exception.ProcessingException;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorDAO doctorDAO;
    @Transactional
    public List<Doctor> findAll() {
        return doctorDAO.findAll();
    }
    @Transactional
    public Optional<Doctor> findById(Integer id) {
        Optional<Doctor> doctor = doctorDAO.findById(id);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Could not find doctor with id: [%s]".formatted(id));
        }
        return doctor;
    }
    public Doctor findByEmail(String doctorEmail) {
        Optional<Doctor> doctor = doctorDAO.findByEmail(doctorEmail);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Could not find doctor with email: [%s]".formatted(doctorEmail));
        }
        return doctor.get();
    }
    public List<Doctor> findDoctorsBySpecializationName(String specialization) {
        return doctorDAO.findBySpecializationName(specialization);
    }

    public void saveSpecToDoctor(Doctor doctor, Specialization specialization) {
        Set<Specialization> specializationsSet = doctor.getSpecializations();
        specializationsSet.forEach(x -> {
            if (x.getName().equalsIgnoreCase(specialization.getName()) && x.getSpecializationId() != null) {
                throw new ProcessingException("Doctor already have that specialization: [%s]".formatted(specialization.getName()));
            } else {
                doctorDAO.saveSpecToDoctor(doctor, specialization);
            }
        });
    }
    @Transactional
    public void save(Doctor doctor) {
        doctorDAO.save(doctor);
    }
}
