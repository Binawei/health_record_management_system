package com.example.devohealthrecord.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HealthRecordResponse {
    private String patientNumber;
    private String userEmail;
    private String doctorEmail;
    private String userName;
    private String patientId;
    private String genoType;
    private String bloodGroup;
    private Integer pulseRate;
    private Integer respirationRate;
    private String bodyTemperature;
    private String medications;
}
