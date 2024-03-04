package pl.clinic.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.clinic.infrastructure.database.entity.AppointmentEntity;

import java.util.List;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Integer> {
    @Query("""
            SELECT a FROM AppointmentEntity a
            WHERE a.patient.email = :email
            AND a.isFinished = false
            ORDER BY a.reservedDate ASC, a.reservedStartTime ASC
            """)
    List<AppointmentEntity> findByPatientEmailAndIsFinishedFalse(@Param("email")String email);
    @Query("""
            SELECT a FROM AppointmentEntity a
            WHERE a.patient.email = :email
            AND a.isFinished = true
            ORDER BY a.reservedDate ASC, a.reservedStartTime ASC
            """)
    List<AppointmentEntity> findByPatientEmailAndIsFinishedTrue(@Param("email")String email);
    @Modifying
    @Query("""
            DELETE FROM AppointmentEntity a
            where a.appointmentId = :appointmentId
            """)
    void deleteQuery(@Param("appointmentId")Integer appointmentId);
}
