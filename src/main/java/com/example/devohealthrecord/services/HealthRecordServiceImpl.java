package com.example.devohealthrecord.services;

import com.example.devohealthrecord.dto.request.HealthRecordDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.HealthRecordResponse;
import com.example.devohealthrecord.entities.AppUser;
import com.example.devohealthrecord.entities.Appointment;
import com.example.devohealthrecord.entities.Doctor;
import com.example.devohealthrecord.entities.HealthRecord;
import com.example.devohealthrecord.enums.AppointmentType;
import com.example.devohealthrecord.enums.Role;
import com.example.devohealthrecord.exception.CommonApplicationException;
import com.example.devohealthrecord.repository.AppointmentRepository;
import com.example.devohealthrecord.repository.DoctorRepository;
import com.example.devohealthrecord.repository.HealthRecordRepository;
import com.example.devohealthrecord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthRecordServiceImpl implements HealthRecordServics{
    private final HealthRecordRepository healthRecordRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public GenericResponse createHealthRecord(HealthRecordDTO recordDTO, String email) {
        log.info("entering create record at service ......");
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(email);
        if (optionalDoctor.isEmpty()) {
            return new GenericResponse("00", "User does Exist", HttpStatus.FOUND, "success");
        }
        Doctor doctor = optionalDoctor.get();
        if (!(doctor.getRole().equals(Role.DOCTOR))) {
            return new GenericResponse("00", "Does not have the right to create a record", HttpStatus.UNAUTHORIZED, "success");
        }
        Optional<AppUser> optionalAppUser = userRepository.findByPatientId(recordDTO.getPatientNumber());
        if (optionalAppUser.isEmpty()) {
            return new GenericResponse("00", "User is not yet our patient", HttpStatus.FOUND, "success");
        }
        AppUser user = optionalAppUser.get();
        log.info(user.getEmail() + " " + user.getFullName());
        List<Appointment> userAppointments = user.getAppointments();
        HealthRecord healthRecord = HealthRecord.builder().appointments(new ArrayList<>())
                .bloodGroup(recordDTO.getBloodGroup()).bodyTemperature(recordDTO.getBodyTemperature())
                .genoType(recordDTO.getGenoType()).medications(recordDTO.getMedications()).respirationRate(recordDTO.getRespirationRate())
                .pulseRate(recordDTO.getPulseRate()).user(user).patientId(user.getPatientId())
                .doctor(doctor)
          .build();
        for (Appointment appointment : userAppointments) {
            if (appointment.getAppointmentType() == AppointmentType.PATIENT) {
                appointment.setHealthRecord(healthRecord);
                healthRecord.getAppointments().add(appointment);
            }
        }
        healthRecordRepository.save(healthRecord);

            HealthRecordResponse response = HealthRecordResponse.builder().doctorEmail(doctor.getEmail())
                    .patientNumber(user.getPatientId()).userEmail(user.getEmail()).userName(user.getFullName())
                    .respirationRate(healthRecord.getRespirationRate()).pulseRate(healthRecord.getPulseRate()) .build();

            return new GenericResponse<>("00", "Success", response, "Health Record created successfully", HttpStatus.OK);

        }

    @Override
    public GenericResponse<?> findHealthRecordById(Long recordId) throws CommonApplicationException {
        Optional<HealthRecord> recordOptional = healthRecordRepository.findById(recordId);


        if (recordOptional.isPresent()) {
            log.info("Found record with recordId {}", recordId);
           HealthRecord record = recordOptional.get();
            log.info(record.toString());
            HealthRecordResponse response = HealthRecordResponse.builder().doctorEmail(record.getDoctor().getEmail())
                    .userName(record.getUser().getFullName())
                    .bloodGroup(record.getBloodGroup()).bodyTemperature(record.getBodyTemperature())
                    .patientNumber(record.getPatientId()).medications(record.getMedications())
                    .genoType(record.getGenoType()).pulseRate(record.getPulseRate())
                    .respirationRate(record.getRespirationRate()).build();
            return new GenericResponse<>("00", "Success", response, "OK");
        } else {
            throw new CommonApplicationException("Order does not exist");
        }
    }

    @Override
    public GenericResponse<?> UserFindHealthRecordById(Long recordId, String email) throws CommonApplicationException {
        Optional<HealthRecord> optionalHealthRecord = healthRecordRepository.findById(recordId);
        Optional<AppUser>optionalAppUser = userRepository.findByEmail(email);
        if(optionalAppUser.isEmpty()) {
            return new GenericResponse<>("00", "User not found", HttpStatus.NOT_FOUND);
        }
        AppUser user = optionalAppUser.get();
       if (optionalHealthRecord.isEmpty()) {
           return new GenericResponse<>("00", "Record not found", HttpStatus.NOT_FOUND);
       }
        log.info("Found record with recordId {}", recordId);
        HealthRecord record = optionalHealthRecord.get();
        if(!(user.getPatientId().equals(record.getPatientId()))) {
            return new GenericResponse<>("00", "User is not the owner of the record", HttpStatus.NOT_FOUND);
        }
        log.info("Record found is "+record.toString());
        HealthRecordResponse response = HealthRecordResponse.builder().doctorEmail(record.getDoctor().getEmail())
                    .userName(record.getUser().getFullName())
                    .bloodGroup(record.getBloodGroup()).bodyTemperature(record.getBodyTemperature())
                    .patientNumber(record.getPatientId()).medications(record.getMedications())
                    .genoType(record.getGenoType()).pulseRate(record.getPulseRate())
                    .respirationRate(record.getRespirationRate()).build();
            return new GenericResponse<>("00", "Success", response, "OK");

    }

    @Override
    public GenericResponse<Page<HealthRecord>> viewAllHealthRecords(int page, int size) throws CommonApplicationException {
        Pageable pageable = PageRequest.of(page, size);
        Page<HealthRecord> healthRecords = healthRecordRepository.findAll(pageable);

        if (healthRecords.hasContent()) {
            return new GenericResponse<>("00", "Success", healthRecords, "OK");
        } else {
            throw new CommonApplicationException("No orders found");
        }
    }



    @Override
    public GenericResponse<HealthRecordResponse> updateHealthRecord(Long recordId, HealthRecordDTO request, String email) {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return new GenericResponse<>("10", "User not found", HttpStatus.NOT_FOUND);
        }
        AppUser user = userOptional.get();
        Optional<HealthRecord> recordOptional = healthRecordRepository.findById(recordId);
        if(recordOptional.isEmpty()){
            return new GenericResponse<>("10", "Record not found", HttpStatus.NOT_FOUND);
        }
        HealthRecord record = recordOptional.get();
        record.setAppointmentDate(request.getAppointmentDate());
        record.setMedications(request.getMedications());
        record.setGenoType(request.getGenoType());
        record.setBloodGroup(request.getBloodGroup());
        record.setPatientId(request.getPatientNumber());
        record.setBodyTemperature(request.getBodyTemperature());
        record.setRespirationRate(request.getRespirationRate());
        record.setPulseRate(request.getPulseRate());
        healthRecordRepository.save(record);

        HealthRecordResponse response = HealthRecordResponse.builder().doctorEmail(email)
                .patientNumber(user.getPatientId()).userEmail(user.getEmail()).userName(user.getFullName())
                .build();

        return new GenericResponse<>("00", "Success", response, "Health Record updated successfully", HttpStatus.OK);
    }

    @Override
    public GenericResponse<String> deleteRecord(Long recordId, String userEmail) {
        log.info("Checking if order exists");
        Optional<HealthRecord> optionalOrder = healthRecordRepository.findById(recordId);
        if (optionalOrder.isEmpty()) {
            log.error("Order not found for ID: {}", recordId);
            return new GenericResponse<>("10", "Order not found", HttpStatus.NOT_FOUND);
        }
        HealthRecord healthRecord = optionalOrder.get();
        log.info("Checking if the user has permission to delete the order");

        if (!userEmail.equals(healthRecord.getUser().getEmail())) {
            log.error("User is not the creator of this record");
            return new GenericResponse<>("10", "User is not a Doctor", HttpStatus.FORBIDDEN);
        }

        log.info("Deleting the order");
        healthRecordRepository.deleteById(recordId);
        log.info("Order deleted successfully");

        return new GenericResponse<>("00", "Record deleted successfully", HttpStatus.OK);
    }
}
