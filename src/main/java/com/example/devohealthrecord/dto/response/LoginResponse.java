package com.example.devohealthrecord.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse extends GenericResponse{
    private String token;

    public LoginResponse(Map map) {
        super(map);
    }

    public LoginResponse(String code, String message, Object data, String status, HttpStatus httpStatus) {
        super(code, message, data, status, httpStatus);
    }

}
