package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;

import java.util.Optional;

@Repository
public interface SpecializationJpaRepository extends JpaRepository<SpecializationEntity, Integer> {

    Optional<SpecializationEntity> findByName(@Param("name")String name);
}
