package pl.clinic.domain;

import lombok.*;

import java.util.List;

@With
@Value
@Builder
@EqualsAndHashCode(of = "doctorId")
@ToString(of = {"doctorId", "name", "surname", })
public class DoctorWithSpecializations {
    Integer doctorId;
    String name;
    String surname;
    List<Specialization> specializations;
}
