package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.DoctorEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Integer> {

    Optional<DoctorEntity> findByEmail(String email);

    @Query("""
            SELECT d FROM DoctorEntity d
            JOIN d.specializations s
            WHERE s.name = :specialization
            """)
    List<DoctorEntity> findBySpecializationName(String specialization);
}
