package pl.clinic.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.clinic.api.dto.PatientDTO;
import pl.clinic.api.dto.PatientRestDTO;
import pl.clinic.domain.Patient;
import pl.clinic.domain.PatientRest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO map(Patient patient);
    default List<PatientDTO> map(List<Patient> patients) {
        return patients.stream().map(this::map).toList();
    }
    PatientRest map(PatientRestDTO addressDTO);
    PatientRestDTO map(PatientRest addressDTO);
}
