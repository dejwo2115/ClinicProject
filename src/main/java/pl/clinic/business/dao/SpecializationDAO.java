package pl.clinic.business.dao;

import pl.clinic.domain.Specialization;

import java.util.List;
import java.util.Optional;

public interface SpecializationDAO {
    List<Specialization> findAll();
    Optional<Specialization> findBySpecializationName(String name);
    void save(String specialization);
}
