package pl.clinic.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "specializationId")
@ToString(of = {"specializationId", "name"})
public class Specialization {
    Integer specializationId;
    String name;
}
