package com.example.devohealthrecord.entities;

import com.example.devohealthrecord.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_record_id")
    @JsonIgnore
    private HealthRecord healthRecord;
    private String healthChallenge;
    private String patientId;
    private String location;
    private String selectedTime;
    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime appointmentDate;

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", user=" + (user != null ? user.getId() : "null") +
                ", healthChallenge='" + healthChallenge + '\'' +
                ", patientID='" + patientId + '\'' +
                ", location='" + location + '\'' +
                ", selectedTime='" + selectedTime + '\'' +
                ", appointmentType=" + appointmentType +
                ", appointmentDate=" + appointmentDate +
                '}';
    }

}
