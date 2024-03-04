package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = "prescriptionCode")
@ToString(of = {"prescriptionId", "prescriptionCode", "issueDate", "prescribedMedications", "note"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription")
public class PrescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Integer prescriptionId;
    @Column(name = "prescription_code", unique = true)
    private String prescriptionCode;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "prescribed_medications")
    private String prescribedMedications;
    @Column(name = "note")
    private String note;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private AppointmentEntity appointment;
}
