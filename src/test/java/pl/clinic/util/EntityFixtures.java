package pl.clinic.util;

import lombok.experimental.UtilityClass;
import pl.clinic.infrastructure.database.entity.*;
import pl.clinic.infrastructure.security.UserEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@UtilityClass
public class EntityFixtures {

    public static DoctorEntity someDoctor() {
        return DoctorEntity.builder()
                .name("Dawid")
                .surname("Doktorski")
                .gender("Male")
                .email("doktorski@gmail.com")
                .specializations(Set.of(SpecializationEntity.builder()
                        .name("Dientist")
                        .build()))
                .birthDate(LocalDate.of(1995, 1, 5))
                .build();
    }

    public static UserEntity someDoctorUser(DoctorEntity doctor) {
        return UserEntity.builder()
                .active(true)
                .userName(doctor.getName().toLowerCase() + "_" + doctor.getSurname().toLowerCase())
                .email(doctor.getEmail())
                .password("test")
                .build();
    }

    public static PatientEntity somePatient() {
        return PatientEntity.builder()
                .name("Mare")
                .surname("Marecki")
                .gender("Male")
                .address(AddressEntity.builder()
                        .country("Poland")
                        .city("Suwalki")
                        .postalCode("16-400")
                        .address("Adresowa 15/15")
                        .build())
                .birthDate(LocalDate.of(1992, 3, 4))
                .phone("+48 123 123 123")
                .email("some@email.com")
                .build();
    }

    public static UserEntity somePatientUser(PatientEntity patient) {
        return UserEntity.builder()
                .active(true)
                .userName(patient.getName().toLowerCase() + "_" + patient.getSurname().toLowerCase())
                .email(patient.getEmail())
                .password("test")
                .build();
    }

    public static DoctorAvailabilityEntity someDoctorAvailabilityWithDoctor() {
        return DoctorAvailabilityEntity.builder()
                .reserved(false)
                .doctor(someDoctor())
                .availabilityDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
                .startTime(LocalTime.of(7,0,0))
                .build();
    }
    public static DoctorAvailabilityEntity someDoctorAvailability2() {
        return DoctorAvailabilityEntity.builder()
                .reserved(false)
                .availabilityDate(LocalDate.now().plus(2, ChronoUnit.DAYS))
                .startTime(LocalTime.of(7,30,0))
                .build();
    }
    public static DoctorAvailabilityEntity someDoctorAvailability3() {
        return DoctorAvailabilityEntity.builder()
                .reserved(false)
                .availabilityDate(LocalDate.now().plus(3, ChronoUnit.DAYS))
                .startTime(LocalTime.of(8,0,0))
                .build();
    }
    public static PrescriptionEntity somePrescription(AppointmentEntity appointment) {
        return PrescriptionEntity.builder()
                .issueDate(appointment.getReservedDate())
                .expirationDate(appointment.getReservedDate().plusYears(1))
                .note("some note")
                .prescribedMedications("rutinoscorbin")
                .prescriptionCode("somePrescriptionCode1234124")
                .build();
    }
    public static AppointmentEntity someAppointment(DoctorAvailabilityEntity doctorAvailability) {
        return AppointmentEntity.builder()
                .canceled(false)
                .doctorAvailability(doctorAvailability)
                .isFinished(false)
                .reservedDate(doctorAvailability.getAvailabilityDate())
                .reservedStartTime(doctorAvailability.getStartTime())
                .note("some note")
                .build();
    }
}
