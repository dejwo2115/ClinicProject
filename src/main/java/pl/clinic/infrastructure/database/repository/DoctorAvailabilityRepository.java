package pl.clinic.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.dao.DoctorAvailabilityDAO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.exception.ProcessingException;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;
import pl.clinic.infrastructure.database.repository.jpa.DoctorAvailabilityJpaRepository;
import pl.clinic.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.clinic.infrastructure.database.repository.mapper.DoctorAvailabilityEntityMapper;
import pl.clinic.infrastructure.database.repository.mapper.DoctorEntityMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DoctorAvailabilityRepository implements DoctorAvailabilityDAO {
    private final DoctorAvailabilityJpaRepository doctorAvailabilityJpaRepository;
    private final DoctorAvailabilityEntityMapper doctorAvailabilityEntityMapper;
    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorEntityMapper doctorEntityMapper;

    @Override
    @Transactional
    public void createAvailabilityForDay(Doctor doctor, List<DoctorAvailability> doctorAvailability) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        DoctorEntity saved = doctorJpaRepository.saveAndFlush(doctorEntity);

        doctorAvailability.stream().map(doctorAvailabilityEntityMapper::mapToEntity)
                .forEach(entity -> {
                    DoctorAvailabilityEntity byDoctorAndAvailabilityDateAndStartTime = doctorAvailabilityJpaRepository
                            .findByDoctorAndAvailabilityDateAndStartTime(saved, entity.getAvailabilityDate(), entity.getStartTime()).orElse(null);
                    if(byDoctorAndAvailabilityDateAndStartTime != null){
                        throw new ProcessingException("You have already defined work hours for day: [%s]".formatted(entity.getAvailabilityDate()));
                    }else {
                        entity.setDoctor(saved);
                        doctorAvailabilityJpaRepository.saveAndFlush(entity);
                    }
                });

        }
    @Override
    public Optional<DoctorAvailability> findById(Integer id) {
        return doctorAvailabilityJpaRepository.findById(id)
                .map(doctorAvailabilityEntityMapper::mapFromEntity);
    }

    @Override
    public List<DoctorAvailability> findByDoctor(Doctor doctor) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        return doctorAvailabilityJpaRepository.findByDoctorOrderByAvailabilityDateAscStartTimeAsc(doctorEntity).stream()
                .map(doctorAvailabilityEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<LocalTime> findTimesForDate(Doctor doctor, LocalDate date) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        return doctorAvailabilityJpaRepository.findStartTimesByDoctorAndDate(doctorEntity, date);
    }

    @Override
    public Optional<DoctorAvailability> findByDoctorAndDateAndTime(Doctor doctor, LocalDate selectedDate, LocalTime selectedTime) {
        DoctorEntity doctorEntity = doctorEntityMapper.mapToEntity(doctor);
        return doctorAvailabilityJpaRepository.findByDoctorAndAvailabilityDateAndStartTime(doctorEntity, selectedDate, selectedTime)
                .map(doctorAvailabilityEntityMapper::mapFromEntity);
    }
}
