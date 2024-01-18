package com.example.devohealthrecord.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationResponseDTO {
    private String fullName;
    private String patientNumber;

    @Override
    public String toString() {
        return "Your details are: {" +
                "fullName='" + fullName + '\'' +
                ", patientNumber='" + patientNumber + '\'' +
                '}';
    }
}
