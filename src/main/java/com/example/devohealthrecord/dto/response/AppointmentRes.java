package com.example.devohealthrecord.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentRes {
    private String selectedTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime appointmentDate;
    private String patientId;
    private String genoType;
    private String bloodGroup;
    private Integer pulseRate;
    private Integer respirationRate;
    private String bodyTemperature;
    private String medications;
}
