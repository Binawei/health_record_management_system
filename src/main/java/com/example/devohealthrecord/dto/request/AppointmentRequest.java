package com.example.devohealthrecord.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {
    private String healthChallenge;
    private String location;
    private LocalDateTime appointmentDate;
    private String email;
}
