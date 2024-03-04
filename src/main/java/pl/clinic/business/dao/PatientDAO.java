package pl.clinic.business.dao;

import pl.clinic.domain.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    void savePatient(Patient patient);
    Optional<Patient> findByEmail(String email);
    List<Patient> findAll();
}
