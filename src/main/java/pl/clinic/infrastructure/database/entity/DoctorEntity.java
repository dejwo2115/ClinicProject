package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "doctorId")
@ToString(of = {"doctorId", "name", "surname", "birthDate", "gender", "email"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor")
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<DoctorAvailabilityEntity> doctorAvailabilities;
    @Column(name = "user_id")
    private Integer userId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctor_specialization",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<SpecializationEntity> specializations;
}
