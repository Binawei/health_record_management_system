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
public class HealthRecordDTO {
    private String genoType;
    private String bloodGroup;
    private Integer pulseRate;
    private Integer respirationRate;
    private String bodyTemperature;
    private String medications;
    private String patientNumber;
}
