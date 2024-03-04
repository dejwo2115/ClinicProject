package pl.clinic.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.PatientDAO;
import pl.clinic.domain.Patient;
import pl.clinic.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {
    private final PatientDAO patientDAO;
    @Transactional
    public void savePatient(Patient patient) {
        patientDAO.savePatient(patient);
    }
    @Transactional
    public Patient findPatientByEmail(String email) {
        Optional<Patient> patient = patientDAO.findByEmail(email);
        if (patient.isEmpty()) {
            throw new NotFoundException("Could not find patient by email: [%s]".formatted(email));
        }
        return patient.get();
    }
    @Transactional
    public List<Patient> findAll() {
        return patientDAO.findAll();
    }
}
