package pl.clinic.util;

import lombok.experimental.UtilityClass;
import pl.clinic.api.dto.*;
import pl.clinic.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@UtilityClass
public class TestClassesFixtures {

    public static Patient buildPatient() {
        return Patient.builder()
                .name("Mare")
                .surname("Marecki")
                .gender("Male")
                .userId(1)
                .address(Address.builder()
                        .country("Poland")
                        .city("Suwalki")
                        .postalCode("16-400")
                        .address("Adresowa 15/15")
                        .build())
                .birthDate(LocalDate.of(1992,3,4))
                .phone("+48 123 123 123")
                .email("some@email.com")
                .build();
    }
    public static PatientDTO buildPatientDTO() {
        return PatientDTO.builder()
                .name("Mare")
                .surname("Marecki")
                .gender("Male")
                .birthDate(LocalDate.of(1992,3,4))
                .phone("+48 123 123 123")
                .email("some@email.com")
                .build();
    }
    public static Prescription buildPrescription(Appointment appointment) {
        return Prescription.builder()
                .issueDate(appointment.getReservedDate())
                .expirationDate(appointment.getReservedDate().plusYears(1))
                .note("some note")
                .prescribedMedications("rutinoscorbin")
                .prescriptionCode("somePrescriptionCode")
                .build();
    }
    @SuppressWarnings("unused")
    public static PrescriptionDTO buildPrescriptionDTO(AppointmentDTO appointment) {
        return PrescriptionDTO.builder()
                .prescriptionId(1)
                .issueDate(appointment.getReservedDate())
                .expirationDate(appointment.getReservedDate().plusYears(1))
                .note("some note")
                .prescribedMedications("rutinoscorbin")
                .prescriptionCode("somePrescriptionCode")
                .build();
    }
    public static Appointment buildNotProceededAppointment(DoctorAvailability doctorAvailability) {
        return Appointment.builder()
                .canceled(false)
                .patient(buildPatient())
                .doctorAvailability(doctorAvailability)
                .isFinished(false)
                .reservedDate(doctorAvailability.getAvailabilityDate())
                .reservedStartTime(doctorAvailability.getStartTime())
                .note("some note")
                .build();
    }
    public static AppointmentDTO buildNotProceededAppointmentDTO(DoctorAvailabilityDTO doctorAvailabilityDTO) {
        return AppointmentDTO.builder()
                .appointmentId(1)
                .canceled(false)
                .isFinished(false)
                .reservedDate(doctorAvailabilityDTO.getAvailabilityDate())
                .reservedStartTime(doctorAvailabilityDTO.getStartTime())
                .note("some note")
                .build();
    }


    public static DoctorDTO buildDoctorDTO() {
        return DoctorDTO.builder()
                .name("Dawid")
                .surname("Doktorski")
                .email("doktorski@gmail.com")
                .specializations(Set.of(SpecializationDTO.builder()
                        .name("Dientist")
                        .build()))
                .build();
    }
    public static Doctor buildDoctor() {
        return Doctor.builder()
                .doctorId(1)
                .name("Dawid")
                .surname("Doktorski")
                .gender("Male")
                .email("doktorski@gmail.com")
                .specializations(Set.of(Specialization.builder()
                        .name("Dientist")
                        .build()))
                .birthDate(LocalDate.of(1995, 1, 5))
                .build();
    }

    public static DoctorAvailability buildDoctorAvailability() {
        return DoctorAvailability.builder()
                .doctorAvailabilityId(1)
                .availabilityDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(7, 0, 0))
                .reserved(false)
                .doctor(buildDoctor())
                .build();
    }
    public static DoctorAvailabilityDTO buildDoctorAvailabilityDTO() {
        return DoctorAvailabilityDTO.builder()
                .doctorAvailabilityId(1)
                .availabilityDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(7, 0, 0))
                .reserved(false)
                .build();
    }

    public static DoctorAvailability buildDoctorAvailability(Doctor doctor) {
        return DoctorAvailability.builder()
                .doctorAvailabilityId(1)
                .availabilityDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(9, 0, 0))
                .reserved(false)
                .doctor(doctor)
                .build();
    }
}
