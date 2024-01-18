package com.example.devohealthrecord.controllers;
import com.example.devohealthrecord.dto.request.HealthRecordDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.HealthRecordResponse;
import com.example.devohealthrecord.entities.HealthRecord;
import com.example.devohealthrecord.exception.CommonApplicationException;
import com.example.devohealthrecord.security.JWTService;
import com.example.devohealthrecord.services.HealthRecordServics;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "get Record details by id", notes = "Provide an Id to get the record of that health",
            response = GenericResponse.class)
    public ResponseEntity<GenericResponse<?>> viewHealthRecordBYId(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is viewing health with ID {}", userDetails.get("name"), recordId);
        GenericResponse<?> order = healthRecordServics.findHealthRecordById(recordId);
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }

    @GetMapping("/user/{recordId}")
    public ResponseEntity<GenericResponse<?>> userViewHealthRecordBYId(
            @PathVariable Long recordId,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is viewing health with ID {}", userDetails.get("name"), recordId);
        GenericResponse<?> order = healthRecordServics.UserFindHealthRecordById(recordId, (String) userDetails.get("email"));
        return new ResponseEntity<>(order, HttpStatus.FOUND);
    }

    @GetMapping("/all-records")
    public ResponseEntity<GenericResponse<Page<HealthRecord>>> getAllHealthRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("User {} is retrieving all orders", userDetails.get("name"));
        GenericResponse<Page<HealthRecord>> orders = healthRecordServics.viewAllHealthRecords(page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<GenericResponse<?>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody HealthRecordDTO request, @RequestHeader("Authorization") String authorizationHeader) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("Request for customer {} to delete an order", userDetails.get("name"));
        String userEmail = userDetails.get("email");
        GenericResponse<HealthRecordResponse> updatedOrder = healthRecordServics.updateHealthRecord(orderId, request, userEmail);
        return new ResponseEntity<>(updatedOrder, HttpStatus.CREATED);
    }
}
