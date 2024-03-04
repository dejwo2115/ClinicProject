package pl.clinic.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.PatientDAO;
import pl.clinic.domain.Patient;
import pl.clinic.infrastructure.database.entity.PatientEntity;
import pl.clinic.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.PatientEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDAO {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientEntityMapper patientEntityMapper;
    @Transactional
    @Override
    public void savePatient(Patient patient) {
        PatientEntity toSavePatient = patientEntityMapper.mapToEntity(patient);
        patientJpaRepository.saveAndFlush(toSavePatient);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientJpaRepository.findByEmail(email)
                .map(patientEntityMapper::mapFromEntity);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll().stream().map(patientEntityMapper::mapFromEntity).toList();
    }

}
