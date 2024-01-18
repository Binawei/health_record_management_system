package com.example.devohealthrecord.services;

import com.example.devohealthrecord.entities.Doctor;
import com.example.devohealthrecord.enums.Role;
import com.example.devohealthrecord.enums.Specialization;
import com.example.devohealthrecord.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@Service
public class CreateDoctor {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateDoctor(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        runAtStart();
    }

    public void runAtStart() {
        log.info("Creating admin");
        if(!doctorRepository.existsByEmail("devodoctor@gmail.com")) {
            Doctor doctor = new Doctor();
            doctor.setEmail("devodoctor@gmail.com");
            doctor.setPassword(passwordEncoder.encode("Doctor1234"));
            doctor.setRole(Role.DOCTOR);
            doctor.setCreationDate(LocalDateTime.now());
            doctor.setLastLogin(LocalDateTime.now());
            doctor.setPhoneNumber("09039156872");
            doctor.setFullName("Devo Doctor");
            doctor.setDoctorId("DH"+UserServiceImpl.generatePatientId());
            doctor.setIsVerified(true);
            doctor.setSpecialization(Specialization.Neurology);
            doctorRepository.save(doctor);
        }
    }
}
