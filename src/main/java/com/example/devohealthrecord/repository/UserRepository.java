package com.example.devohealthrecord.repository;

import com.example.devohealthrecord.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPatientId(String patientId);
    Boolean existsByEmail(String email);
}
