package com.example.devohealthrecord.controllers;
import com.example.devohealthrecord.dto.request.LoginDTO;
import com.example.devohealthrecord.dto.request.SignUpDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.LoginResponse;
import com.example.devohealthrecord.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpDTO request) {
        log.info("controller register: register user :: [{}] ::", request.getEmail());
        GenericResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDTO request) {
        log.info("request to login user with "+ request.getEmail());
        GenericResponse<LoginResponse> response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
