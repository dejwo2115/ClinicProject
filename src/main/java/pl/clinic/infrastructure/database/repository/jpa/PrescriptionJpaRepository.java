package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.PrescriptionEntity;
@Repository
@SuppressWarnings("unused")
public interface PrescriptionJpaRepository extends JpaRepository<PrescriptionEntity, Integer> {
}
