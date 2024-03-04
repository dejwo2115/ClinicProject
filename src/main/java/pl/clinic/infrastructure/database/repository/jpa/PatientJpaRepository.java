package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.PatientEntity;

import java.util.Optional;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Integer> {
    Optional<PatientEntity> findByEmail(String email);

}
