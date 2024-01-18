package com.example.devohealthrecord.security;
import com.example.devohealthrecord.entities.AppUser;
import com.example.devohealthrecord.entities.Doctor;
import com.example.devohealthrecord.repository.DoctorRepository;
import com.example.devohealthrecord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@RequiredArgsConstructor
@Component
@Slf4j
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Doctor> doctorOptional = doctorRepository.findByEmail(username);
        if (doctorOptional.isPresent()) {
            return new AppUserDetails(doctorOptional.get());
        }

        Optional<AppUser> patientOptional = userRepository.findByEmail(username);
        if (patientOptional.isPresent()) {
            return new AppUserDetails(patientOptional.get());
        }

        throw new UsernameNotFoundException("User not in Database");
    }



}
