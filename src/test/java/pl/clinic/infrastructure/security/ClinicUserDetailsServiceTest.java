package pl.clinic.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.Doctor;
import pl.clinic.domain.DoctorRest;
import pl.clinic.domain.Patient;
import pl.clinic.domain.PatientRest;
import pl.clinic.domain.exception.ProcessingException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicUserDetailsServiceTest {
    @InjectMocks
    private ClinicUserDetailsService clinicUserDetailsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private DoctorService doctorService;
    @Mock
    private PatientService patientService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername() {
        // given
        String username = "testUser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(username);
        userEntity.setActive(true);
        userEntity.setPassword("$2a$10$eBz2Ry3XU.vzZ1HUYXNmxe2EE1vWtrQSoi2V5sJb2bvZhYiEwpeEu"); // example password hash
        userEntity.setRoles(Set.of(RoleEntity.builder().role("ROLE_USER").build()));

        when(userRepository.findByUserName(username)).thenReturn(userEntity);

        // when
        UserDetails userDetails = clinicUserDetailsService.loadUserByUsername(username);

        // then
        assertEquals(username, userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());

        verify(userRepository).findByUserName(username);
    }

    @Test
    void findDoctorByUserName() {
        // given
        String username = "testUser";
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        Doctor doctor = Doctor.builder().build();
        when(userRepository.findByUserName(username)).thenReturn(userEntity);
        when(doctorService.findByEmail(email)).thenReturn(doctor);

        // when
        Doctor result = clinicUserDetailsService.findDoctorByUserName(username);

        // then
        assertEquals(doctor, result);

        verify(userRepository).findByUserName(username);

        verify(doctorService).findByEmail(email);
    }

    @Test
    void findPatientByUserName() {
        // given
        String username = "testUser";
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        Patient patient = Patient.builder().build();
        when(userRepository.findByUserName(username)).thenReturn(userEntity);
        when(patientService.findPatientByEmail(email)).thenReturn(patient);

        // when
        Patient result = clinicUserDetailsService.findPatientByUserName(username);

        // then
        assertEquals(patient, result);

        verify(userRepository).findByUserName(username);

        verify(patientService).findPatientByEmail(email);
    }

    @Test
    void createUserDoctor() {
        // given
        DoctorRest doctorRest = DoctorRest.builder()
                .email("test@example.com")
                .userName("testUser")
                .password("password")
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 3, 3))
                .gender("Male")
                .build();


        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleRepository.findByRole("DOCTOR")).thenReturn(RoleEntity.builder().build());
        when(passwordEncoder.encode(doctorRest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            userEntity.setId(1);
            return userEntity;
        });
        doNothing().when(doctorService).save(any(Doctor.class));

        // when
        clinicUserDetailsService.createUserDoctor(doctorRest);

        // then
        verify(userRepository).findAll();
        verify(roleRepository).findByRole("DOCTOR");
        verify(passwordEncoder).encode(doctorRest.getPassword());
        verify(userRepository).save(Mockito.any(UserEntity.class));
        verify(doctorService).save(Mockito.any(Doctor.class));
    }

    @Test
    void createUserPatient() {
        // given
        PatientRest patientRest = PatientRest.builder()
                .email("test@example.com")
                .userName("testUser")
                .password("password")
                .name("Alice")
                .surname("Smith")
                .birthDate(LocalDate.of(1995, 5, 15))
                .gender("Female")
                .build();

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(roleRepository.findByRole("PATIENT")).thenReturn(RoleEntity.builder().build());
        when(passwordEncoder.encode(patientRest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            userEntity.setId(1);
            return userEntity;
        });
        doNothing().when(patientService).savePatient(any(Patient.class));

        //when
        clinicUserDetailsService.createUserPatient(patientRest);

        // then
        verify(userRepository).findAll();
        verify(passwordEncoder).encode(patientRest.getPassword());
        verify(userRepository).save(any(UserEntity.class));
        verify(patientService).savePatient(any(Patient.class));
    }
    @Test
    void createUserPatient_UserAlreadyExists() {
        // given
        PatientRest patientRest = PatientRest.builder()
                .email("test@example.com")
                .userName("testUser")
                .password("password")
                .build();

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test@example.com");
        existingUser.setUserName("testUser");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));

        // when, then
        ProcessingException exception = assertThrows(ProcessingException.class, () -> clinicUserDetailsService.createUserPatient(patientRest));
        assertEquals("User with email:[test@example.com] already exist.", exception.getMessage());

        // Verification
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(patientService);
    }
    @Test
    void createUserDoctor_UserAlreadyExists() {
        // given
        DoctorRest doctorRest = DoctorRest.builder()
                .email("test@example.com")
                .userName("testUser")
                .password("password")
                .build();

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test@example.com");
        existingUser.setUserName("testUser");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));

        // when, then
        ProcessingException exception = assertThrows(ProcessingException.class, () -> clinicUserDetailsService.createUserDoctor(doctorRest));
        assertEquals("User with email:[test@example.com] already exist.", exception.getMessage());

        // Verification
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(doctorService);
    }
}
