package com.example.devohealthrecord.services;

import com.example.devohealthrecord.dto.request.AppointmentRequest;
import com.example.devohealthrecord.dto.request.LoginDTO;
import com.example.devohealthrecord.dto.request.SignUpDTO;
import com.example.devohealthrecord.dto.response.AppointmentResponse;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.LoginResponse;
import com.example.devohealthrecord.dto.response.RegistrationResponseDTO;
import com.example.devohealthrecord.entities.AppUser;
import com.example.devohealthrecord.entities.Appointment;
import com.example.devohealthrecord.entities.Doctor;
import com.example.devohealthrecord.entities.TimeSlot;
import com.example.devohealthrecord.enums.AppointmentType;
import com.example.devohealthrecord.enums.Role;
import com.example.devohealthrecord.exception.UserExistException;
import com.example.devohealthrecord.repository.AppointmentRepository;
import com.example.devohealthrecord.repository.DoctorRepository;
import com.example.devohealthrecord.repository.TimeSlotRepository;
import com.example.devohealthrecord.repository.UserRepository;
import com.example.devohealthrecord.security.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public GenericResponse registerUser(SignUpDTO registrationDTO) {
        Optional<AppUser> optionalUser = userRepository.findByEmail(registrationDTO.getEmail());
        if (optionalUser.isPresent()) {
            return new GenericResponse("00", "User Already Exist", HttpStatus.ALREADY_REPORTED, "success");
        }
        AppUser newAppUser = new AppUser();
        newAppUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newAppUser.setFullName(registrationDTO.getFullName());
        newAppUser.setPhoneNumber(registrationDTO.getPhoneNumber());
        newAppUser.setIsVerified(true);
        newAppUser.setEmail(registrationDTO.getEmail());
        newAppUser.setRole(Role.PATIENT);
        newAppUser.setPatientId("HR"+generatePatientId());
        AppUser savedAppUser = userRepository.save(newAppUser);
        log.info(savedAppUser + "saved to database");
        GenericResponse genericResponse = new GenericResponse<>();
        RegistrationResponseDTO responseDTO = RegistrationResponseDTO.builder().fullName(savedAppUser.getFullName())
                .patientNumber(savedAppUser.getPatientId()).build();
        genericResponse.setMessage("Registration Successful welcome to the best health app in the world");
        genericResponse.setStatus("Success");
        genericResponse.setData(responseDTO);
        genericResponse.setCode("00");
        return genericResponse;
    }

    @Override
    public GenericResponse<LoginResponse> login(LoginDTO loginDTO) {
        log.info("Request to login at the service layer");

        Authentication authenticationUser;
        try {
            authenticationUser = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            log.info("Authenticated the User by the Authentication manager");
        } catch (DisabledException es) {
            log.error("DisabledException occurred: {}", es.getMessage());
            return Stream.of(
                            new AbstractMap.SimpleEntry<>("message", "Disabled exception occurred"),
                            new AbstractMap.SimpleEntry<>("status", "failure"),
                            new AbstractMap.SimpleEntry<>("httpStatus", HttpStatus.BAD_REQUEST)
                    )
                    .collect(
                            Collectors.collectingAndThen(
                                    Collectors.toMap(AbstractMap.SimpleEntry::getKey, entry -> entry.getValue()),
                                    map -> new GenericResponse<>((Map<String, String>) map)
                            )
                    );

        } catch (BadCredentialsException e) {
            throw new UserExistException("Invalid email or password", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationUser);
        // Retrieve the user from the repository
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(loginDTO.getEmail());
        Optional<AppUser> optionalAppUser = userRepository.findByEmail(loginDTO.getEmail());

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setLastLogin(LocalDateTime.now());
            log.info("last-login date updated");
            Doctor savedDoctor = doctorRepository.save(doctor);
            log.info("Doctor saved back to the database");
            String tokenGenerated = "Bearer " + jwtService.generateToken(authenticationUser, savedDoctor.getRole());
            log.info("Jwt token generated for the doctor " + tokenGenerated);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(tokenGenerated);

            GenericResponse<LoginResponse> apiResponse = new GenericResponse<>("00", "Success", loginResponse, "Successfully logged in", HttpStatus.OK);
            apiResponse.setData(loginResponse);
            return apiResponse;
        } else if (optionalAppUser.isPresent()) {
            AppUser appUser = optionalAppUser.get();
            appUser.setLastLogin(LocalDateTime.now());
            log.info("last-login date updated");
            AppUser savedAppUser = userRepository.save(appUser);
            log.info("AppUser saved back to the database");
            String tokenGenerated = "Bearer " + jwtService.generateToken(authenticationUser, savedAppUser.getRole());
            log.info("Jwt token generated for the user " + tokenGenerated);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(tokenGenerated);

            GenericResponse<LoginResponse> apiResponse = new GenericResponse<>("00", "Success", loginResponse, "Successfully logged in", HttpStatus.OK);
            apiResponse.setData(loginResponse);
            return apiResponse;
        } else {
            throw new UserExistException("User not found", HttpStatus.BAD_REQUEST);
        }
    }


    public static String generatePatientId() {
        Random random = new Random();
        int idLength = 6;
        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < idLength; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }
        return otpBuilder.toString();
    }
    @Override
    public GenericResponse BookAppointment(AppointmentRequest request, String email){
        Optional<AppUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return new GenericResponse("00", "User does Exist", HttpStatus.FOUND, "success");
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(request.getEmail());
        if(optionalDoctor.isEmpty()){
            return new GenericResponse("00", "Doctor does Exist", HttpStatus.FOUND, "success");
        }
        Doctor doctor = optionalDoctor.get();
        AppUser user = optionalUser.get();
        String patientNumber = user.getPatientId();
        List<TimeSlot> availableTimeSlots = timeSlotRepository.findAll();
        TimeSlot selectedTimeSlot = getRandomTimeSlot(availableTimeSlots);

        Appointment appointment = Appointment.builder().appointmentDate(request.getAppointmentDate())
                .user(user).location(request.getLocation()).patientId(patientNumber).appointmentType(AppointmentType.PATIENT)
                .selectedTime(selectedTimeSlot.toString()).healthChallenge(request.getHealthChallenge())
                .doctor(doctor)
                .build();
       appointmentRepository.save(appointment);
        AppointmentResponse response = AppointmentResponse.builder().healthChallenge(request.getHealthChallenge())
                .location(request.getLocation()).userEmail(user.getEmail()).patientID(patientNumber)
                .selectedTime(appointment.getSelectedTime().toString()).doctorEmail(doctor.getEmail())
                .build();
       return new GenericResponse<>("00", "Success", response, "Appointed booked successfully", HttpStatus.OK);

    }
    private TimeSlot getRandomTimeSlot(List<TimeSlot> timeSlots) {
        Random random = new Random();
        return timeSlots.get(random.nextInt(timeSlots.size()));
    }
}
