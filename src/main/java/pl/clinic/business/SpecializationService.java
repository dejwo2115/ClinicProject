package pl.clinic.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.clinic.business.dao.SpecializationDAO;
import pl.clinic.domain.Specialization;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SpecializationService {
    private final SpecializationDAO specializationDAO;


    public List<Specialization> findAll() {
        return specializationDAO.findAll();
    }

    public Boolean isNewSpecialization(Specialization specialization) {
        Optional<Specialization> bySpecializationName = specializationDAO.findBySpecializationName(specialization.getName());
        return bySpecializationName.filter(value -> !value.getName().equalsIgnoreCase(specialization.getName())).isPresent();

    }

    public void save(String specialization) {
        specializationDAO.save(specialization);
    }
}
