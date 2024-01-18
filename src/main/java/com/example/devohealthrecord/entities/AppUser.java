package com.example.devohealthrecord.entities;

import com.example.devohealthrecord.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ApiModel(description ="This is the Patient that will be using the application")
public class AppUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(notes = "The patients firstName and LastName")
    private String fullName;
    @ApiModelProperty(notes = "Users phoneNumber")
    private String phoneNumber;
    private String password;
    @ApiModelProperty(notes = "A unique name generated for the user")
    private String patientId;
    @ApiModelProperty(notes = "The users email")
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLogin;
    @Column
    @CreationTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creationDate;
    private Boolean isVerified = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<HealthRecord> healthRecords;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appointment> appointments;




    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", patientId='" + patientId + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", lastLogin=" + lastLogin +
                ", creationDate=" + creationDate +
                ", isVerified=" + isVerified +
                ", healthRecords.size=" + (healthRecords != null ? healthRecords.size() : "null") +
                ", appointments.size=" + (appointments != null ? appointments.size() : "null") +
                '}';
    }

}
