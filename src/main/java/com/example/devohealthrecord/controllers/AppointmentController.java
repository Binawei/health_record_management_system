package com.example.devohealthrecord.controllers;

import com.example.devohealthrecord.dto.request.AppointmentRequest;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.exception.CommonApplicationException;
import com.example.devohealthrecord.security.JWTService;
import com.example.devohealthrecord.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private final JWTService jwtService;
    private final UserService userService;
    @PostMapping("/book-appointment")
    public ResponseEntity<GenericResponse> createAppointment(@Valid @RequestBody AppointmentRequest request, @RequestHeader("Authorization") String authorizationHeader) throws CommonApplicationException {
        var userDetails = jwtService.validateTokenAndReturnDetail(authorizationHeader.substring(7));
        log.info("request for user {} to book appointment", userDetails.get("name"));
        log.info("request for user with email {} to book appointment", userDetails.get("name"));
        GenericResponse apiResponse = userService.BookAppointment(request, (String) userDetails.get("email"));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
