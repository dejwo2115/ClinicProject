package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "patientId")
@ToString(of = {"patientId", "name", "surname", "birthDate", "email", "phone"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phone;
    @Column(name = "gender")
    private String gender;
    @Column(name = "user_id")
    private Integer userId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<AppointmentEntity> appointments;
}
