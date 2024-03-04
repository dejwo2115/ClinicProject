package pl.clinic.api.dto.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.clinic.api.dto.AppointmentDTO;
import pl.clinic.api.dto.PrescriptionDTO;
import pl.clinic.domain.Appointment;
import pl.clinic.domain.DoctorAvailability;
import pl.clinic.domain.Prescription;

import java.util.List;

import static pl.clinic.util.TestClassesFixtures.*;

class AppointmentDTOMapperTest {
    private final AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    @Test
    void mapToDTOTest() {

        DoctorAvailability doctorAvailability = buildDoctorAvailability();
        Appointment appointment = buildNotProceededAppointment(doctorAvailability);
        Appointment appointment2 = buildNotProceededAppointment(doctorAvailability);
        Prescription prescription = buildPrescription(appointment);
        AppointmentDTO appointmentDTO = appointmentMapper.map(appointment);
        PrescriptionDTO prescriptionDTO = appointmentMapper.map(prescription);
        List<AppointmentDTO> appointmentDTOS = appointmentMapper.map(List.of(appointment2, appointment));
        Assertions.assertThat(appointmentDTO).isNotNull();
        Assertions.assertThat(prescriptionDTO).isNotNull();
        Assertions.assertThat(appointmentDTOS).isNotNull();
    }

    @Test
    void mapFromDTOTest() {
        AppointmentDTO appointmentDTO = AppointmentDTO.builder().appointmentId(1).build();
        PrescriptionDTO prescriptionDTO = PrescriptionDTO.builder().prescriptionCode("something").build();

        Appointment appointment = appointmentMapper.map(appointmentDTO);
        Prescription prescription = appointmentMapper.map(prescriptionDTO);

        Assertions.assertThat(appointment).isNotNull();
        Assertions.assertThat(prescription).isNotNull();
    }

    @Test
    void mapNullValuesWillReturnNull() {
        AppointmentDTO AppointmentDTO = appointmentMapper.map((Appointment) null);
        Appointment appointment = appointmentMapper.map((AppointmentDTO) null);
        List<AppointmentDTO> appointmentDTOS = appointmentMapper.map(((List<Appointment>) null));
        PrescriptionDTO prescriptionDTO = appointmentMapper.map((Prescription) null);
        Prescription prescription = appointmentMapper.map((PrescriptionDTO) null);

        Assertions.assertThat(AppointmentDTO).isNull();
        Assertions.assertThat(appointment).isNull();
        Assertions.assertThat(appointmentDTOS).isNull();
        Assertions.assertThat(prescriptionDTO).isNull();
        Assertions.assertThat(prescription).isNull();




    }
}