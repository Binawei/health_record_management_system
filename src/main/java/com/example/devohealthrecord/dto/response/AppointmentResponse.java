package com.example.devohealthrecord.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AppointmentResponse {
    private String user;
    private String userEmail;
    private String healthChallenge;
    private String patientID;
    private String location;
    private String selectedTime;
    private String doctorEmail;
}
