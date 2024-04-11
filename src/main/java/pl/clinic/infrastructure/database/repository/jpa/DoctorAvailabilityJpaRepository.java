package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.DoctorAvailabilityEntity;
import pl.clinic.infrastructure.database.entity.DoctorEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityJpaRepository extends JpaRepository<DoctorAvailabilityEntity, Integer> {

    List<DoctorAvailabilityEntity> findByDoctorOrderByAvailabilityDateAscStartTimeAsc(DoctorEntity doctor);
    @Query("""
            SELECT da.startTime FROM DoctorAvailabilityEntity da
            WHERE da.doctor = :doctor
            AND da.availabilityDate = :date
            AND da.reserved = false
            """)
    List<LocalTime> findStartTimesByDoctorAndDate(@Param("doctor") DoctorEntity doctor, @Param("date") LocalDate date);
    Optional<DoctorAvailabilityEntity> findByDoctorAndAvailabilityDateAndStartTime(DoctorEntity doctor, LocalDate availabilityDate, LocalTime startTime);
}
