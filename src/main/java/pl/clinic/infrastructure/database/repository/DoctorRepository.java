package pl.clinic.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import pl.clinic.business.dao.DoctorDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.Specialization;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.entity.SpecializationEntity;
import pl.clinic.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.SpecializationJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.DoctorEntityMapper;
import pl.clinic.infrastructure.security.ClinicUserDetailsService;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDAO {

    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorEntityMapper doctorEntityMapper;
    private final SpecializationJpaRepository specializationJpaRepository;


    @Override
    public List<Doctor> findAll() {
        return doctorJpaRepository.findAll().stream()
                .map(doctorEntityMapper::mapFromEntity)
                .toList();
    }
    @Override
    public Optional<Doctor> findById(Integer id) {
        return doctorJpaRepository.findById(id).map(doctorEntityMapper::mapFromEntity);
    }
    @Override
    public Optional<Doctor> findByEmail(String doctorEmail) {
        return doctorJpaRepository.findByEmail(doctorEmail).map(doctorEntityMapper::mapFromEntity);
    }

    @Override
    public List<Doctor> findBySpecializationName(String specialization) {
        return doctorJpaRepository.findBySpecializationName(specialization).stream().map(doctorEntityMapper::mapFromEntity).toList();
    }

    @Override
    public void save(Doctor doctor) {
        DoctorEntity toSave = doctorEntityMapper.mapToEntity(doctor);
        doctorJpaRepository.saveAndFlush(toSave);
    }

    @Override
    public void saveSpecToDoctor(Doctor doctor, Specialization specialization) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);

        SpecializationEntity toSave = doctorEntityMapper.mapToEntity(specialization);
        SpecializationEntity saved = specializationJpaRepository.saveAndFlush(toSave);
        doctorEntity.getSpecializations().add(saved);
        doctorJpaRepository.saveAndFlush(doctorEntity);
    }

}
