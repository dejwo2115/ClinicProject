package pl.clinic.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.clinic.business.dao.SpecializationDAO;
import pl.clinic.domain.Specialization;
import pl.clinic.domain.exception.ProcessingException;
import pl.clinic.infrastructure.database.repository.jpa.SpecializationJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.SpecializationEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SpecializationRepository implements SpecializationDAO {
    private final SpecializationJpaRepository specializationJpaRepository;
    private final SpecializationEntityMapper specializationEntityMapper;

    @Override
    public List<Specialization> findAll() {
        return specializationJpaRepository.findAll().stream().map(specializationEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Optional<Specialization> findBySpecializationName(String name) {
        return specializationJpaRepository.findByName(name).map(specializationEntityMapper::mapFromEntity);
    }

    @Override
    public void save(String specialization) {
        Specialization build = Specialization.builder().name(specialization).build();
        boolean present = Optional.ofNullable(specializationJpaRepository.findByName(specialization)).isPresent();
        if (!present) {
            specializationJpaRepository.saveAndFlush(specializationEntityMapper.mapToEntity(build));
        } else {
            throw new ProcessingException("Specialization [%s], is already in database.".formatted(specialization));
        }
    }
}
