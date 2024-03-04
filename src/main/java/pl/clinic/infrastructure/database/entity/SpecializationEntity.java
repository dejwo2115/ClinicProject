package pl.clinic.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "specializationId")
@ToString(of = {"specializationId", "name"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specialization")
public class SpecializationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Integer specializationId;
    @Column(name = "name", unique = true)
    private String name;
}
