package com.example.devohealthrecord.controllers;
import com.example.devohealthrecord.dto.request.HealthRecordDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.HealthRecordResponse;
import com.example.devohealthrecord.entities.HealthRecord;
import com.example.devohealthrecord.exception.CommonApplicationException;
import com.example.devohealthrecord.security.JWTService;
import com.example.devohealthrecord.services.HealthRecordServics;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/health")
public class HealthRecordController {
    private final JWTService jwtService;
    private final HealthRecordServics healthRecordServics;

    @PostMapping("/create-record")
    public ResponseEntity<GenericResponse> createHealthRecord(@Valid @RequestBody HealthRecordDTO request, @RequestHeader("Authorization") String authorizationHeader) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("request for user {} to  create health record", userDetails.get("name"));
        GenericResponse apiResponse = healthRecordServics.createHealthRecord(request, (String) userDetails.get("email"));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<GenericResponse<?>> viewHealthRecordBYId(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is viewing health with ID {}", userDetails.get("name"), recordId);
        GenericResponse<?> record = healthRecordServics.findHealthRecordById(recordId);
        return new ResponseEntity<>(record, HttpStatus.FOUND);
    }

    @GetMapping("/user/{recordId}")
    public ResponseEntity<GenericResponse<?>> userViewHealthRecordBYId(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is viewing health with ID {}", userDetails.get("name"), recordId);
        GenericResponse<?> record = healthRecordServics.UserFindHealthRecordById(recordId, (String) userDetails.get("email"));
        return new ResponseEntity<>(record, HttpStatus.FOUND);
    }

    @GetMapping("/all-records")
    public ResponseEntity<GenericResponse<Page<HealthRecord>>> getAllHealthRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is retrieving all health records", userDetails.get("name"));
        GenericResponse<Page<HealthRecord>> records = healthRecordServics.viewAllHealthRecords(page, size);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<GenericResponse<?>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody HealthRecordDTO request, @RequestHeader("Authorization") String authorizationHeader) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("Request for Dr. {} to delete an health record", userDetails.get("name"));
        String userEmail = userDetails.get("email");
        GenericResponse<HealthRecordResponse> updatedHealthRecord = healthRecordServics.updateHealthRecord(orderId, request, userEmail);
        return new ResponseEntity<>(updatedHealthRecord, HttpStatus.CREATED);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<GenericResponse<String>> deleteHealthRecord(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        log.info("Received request with Authorization Header: {}", authorizationHeader);
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("Request for Dr. {} to delete an Health record", userDetails.get("name"));
        String userEmail = userDetails.get("email");
        GenericResponse<String> apiResponse = healthRecordServics.deleteRecord(recordId, userEmail);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
