package com.example.devohealthrecord.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;
    @OneToMany(mappedBy = "healthRecord", orphanRemoval = true)
    private List<Appointment> appointments;

    private String patientId;
    @Column
    private String genoType;
    @Column
    private String bloodGroup;
    @Column
    private Integer pulseRate;
    @Column
    private Integer respirationRate;
    @Column
    private String bodyTemperature;
    @Column
    private String medications;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser user;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime appointmentDate;


    @Override
    public String toString() {
        return "HealthRecord{" +
                "recordId=" + recordId +
                ", patientId='" + patientId + '\'' +
                ", genoType='" + genoType + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", appointments=" + appointments + '\''+
                ", pulseRate=" + pulseRate +
                ", respirationRate=" + respirationRate +
                ", bodyTemperature='" + bodyTemperature + '\'' +
                ", medications='" + medications + '\'' +
                '}';
    }

}
