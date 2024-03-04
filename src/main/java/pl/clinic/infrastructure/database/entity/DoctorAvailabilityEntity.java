package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(of = "doctorAvailabilityId")
@ToString(of = {"doctorAvailabilityId", "availabilityDate", "startTime", "reserved"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_availability")
public class DoctorAvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_availability_id")
    private Integer doctorAvailabilityId;
    @Column(name = "availability_date")
    private LocalDate availabilityDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "reserved")
    private Boolean reserved;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "doctorAvailability", orphanRemoval = true)
    private AppointmentEntity appointment;
}
