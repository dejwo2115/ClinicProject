package pl.clinic.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.clinic.business.DoctorService;
import pl.clinic.business.PatientService;
import pl.clinic.domain.*;
import pl.clinic.domain.exception.ProcessingException;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClinicUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(userName);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }
    public Doctor findDoctorByUserName(String userName) {
        UserEntity byUserName = userRepository.findByUserName(userName);
        String email = byUserName.getEmail();
        return doctorService.findByEmail(email);
    }
    public Patient findPatientByUserName(String userName) {
        UserEntity byUserName = userRepository.findByUserName(userName);
        String email = byUserName.getEmail();
        return patientService.findPatientByEmail(email);
    }
    private List<GrantedAuthority> getUserAuthority(Set<RoleEntity> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .distinct()
                .collect(Collectors.toList());
    }
    private UserDetails buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
        return new User(
                user.getUserName(),
                user.getPassword(),
                user.getActive(),
                true,
                true,
                true,
                authorities
        );
    }
    @Transactional
    public void createUserDoctor(DoctorRest doctorRest) {
        List<UserEntity> all = userRepository.findAll();
        all.forEach(user -> {
            if (user.getEmail().equalsIgnoreCase(doctorRest.getEmail())) {
                throw new ProcessingException("User with email:[%s] already exist.".formatted(doctorRest.getEmail()));
            }
        });
        UserEntity newDoctorUser = UserEntity.builder()
                .roles(Set.of(roleRepository.findByRole("DOCTOR")))
                .active(true)
                .email(doctorRest.getEmail())
                .userName(doctorRest.getUserName())
                .password(passwordEncoder.encode(doctorRest.getPassword()))
                .build();
        UserEntity saved = userRepository.save(newDoctorUser);
        Doctor doctor = Doctor.builder()
                .name(doctorRest.getName())
                .surname(doctorRest.getSurname())
                .birthDate(doctorRest.getBirthDate())
                .email(doctorRest.getEmail())
                .gender(doctorRest.getGender())
                .userId(saved.getId())
                .build();
        doctorService.save(doctor);
    }
    @Transactional
    public void createUserPatient(PatientRest patientRest) {
        List<UserEntity> all = userRepository.findAll();
        all.forEach(user -> {
            if (user.getEmail().equalsIgnoreCase(patientRest.getEmail())) {
                throw new ProcessingException("User with email:[%s] already exist.".formatted(patientRest.getEmail()));
            }
        });
        UserEntity newPatientUser = UserEntity.builder()
                .roles(Set.of(roleRepository.findByRole("PATIENT")))
                .active(true)
                .email(patientRest.getEmail())
                .userName(patientRest.getUserName())
                .password(passwordEncoder.encode(patientRest.getPassword()))
                .build();

        UserEntity saved = userRepository.save(newPatientUser);
        Patient patient = Patient.builder()
                .name(patientRest.getName())
                .surname(patientRest.getSurname())
                .birthDate(patientRest.getBirthDate())
                .email(patientRest.getEmail())
                .phone(patientRest.getPhone())
                .gender(patientRest.getGender())
                .userId(saved.getId())
                .address(Address.builder()
                        .country(patientRest.getCountry())
                        .city(patientRest.getCity())
                        .address(patientRest.getAddress())
                        .postalCode(patientRest.getPostalCode())
                        .build())
                .build();
        patientService.savePatient(patient);
    }
}

