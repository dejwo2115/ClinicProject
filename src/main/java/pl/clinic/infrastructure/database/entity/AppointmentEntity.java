package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(of = "appointmentId")
@ToString(of = {
        "appointmentId",
        "reservedDate",
        "reservedStartTime",
        "isFinished",
        "canceled",
        "note"
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;
    @Column(name = "reserved_date")
    private LocalDate reservedDate;
    @Column(name = "reserved_start_time")
    private LocalTime reservedStartTime;
    @Column(name = "is_finished")
    private Boolean isFinished;
    @Column(name = "canceled")
    private Boolean canceled;
    @Column(name = "note")
    private String note;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doctor_availability_id")
    private DoctorAvailabilityEntity doctorAvailability;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prescription_id")
    private PrescriptionEntity prescription;
}
