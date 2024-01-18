package com.example.devohealthrecord.services;

import com.example.devohealthrecord.dto.request.AppointmentRequest;
import com.example.devohealthrecord.dto.request.LoginDTO;
import com.example.devohealthrecord.dto.request.SignUpDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.LoginResponse;

public interface UserService {
    GenericResponse registerUser(SignUpDTO registrationDTO);
    GenericResponse<LoginResponse> login(LoginDTO loginDTO);

    GenericResponse BookAppointment(AppointmentRequest request, String email);
}
