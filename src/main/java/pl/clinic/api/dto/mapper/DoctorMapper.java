package pl.clinic.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.clinic.api.dto.DoctorAvailabilityDTO;
import pl.clinic.api.dto.DoctorDTO;
import pl.clinic.api.dto.DoctorRestDTO;
import pl.clinic.api.dto.SpecializationDTO;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.DoctorRest;
import pl.clinic.domain.Specialization;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "specializations", target = "specializations", qualifiedByName = "mapSpecs")
    DoctorDTO map(final Doctor doctor);
    DoctorAvailabilityDTO mapDA(DoctorAvailability doctorAvailability);
    DoctorRest map(DoctorRestDTO doctorRestDTO);
    default List<DoctorDTO> mapDoctors(List<Doctor> doctorDTOS) {
        return doctorDTOS.stream().map(this::map).toList();
    }
    SpecializationDTO mapSpec(Specialization specialization);

    @Mapping(target = "specializationId", ignore = true)
    Specialization mapSpec(SpecializationDTO specialization);
    @Named("mapSpecs")
    default Set<SpecializationDTO> map(Set<Specialization> specializations) {
        return specializations.stream().map(this::mapSpec).collect(Collectors.toSet());
    }
}
