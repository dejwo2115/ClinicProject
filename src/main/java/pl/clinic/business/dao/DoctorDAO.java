package pl.clinic.business.dao;

import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;

import java.util.List;
import java.util.Optional;

public interface DoctorDAO {
    List<Doctor> findAll();
    Optional<Doctor> findById(Integer id);
    Optional<Doctor> findByEmail(String doctorEmail);
    List<Doctor> findBySpecializationName(String specialization);
    void save(Doctor doctor);
    void saveSpecToDoctor(Doctor doctor, Specialization specialization);

}
