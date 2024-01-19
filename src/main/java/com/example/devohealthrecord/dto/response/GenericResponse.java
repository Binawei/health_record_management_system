package com.example.devohealthrecord.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GenericResponse<T> {
    private String code;
    private String message;
    private T data;
    private String status;

    @JsonIgnore
    private HttpStatus httpStatus;


    public GenericResponse(String code, String message, T data, String status) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.status = status;
    }
    public GenericResponse(String code, String message,HttpStatus httpStatus, String status) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.status = status;
    }


    public GenericResponse(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public GenericResponse(Map<String, String> map) {
        this.status = map.get("status");
        this.message = map.get("message");
    }

    public GenericResponse(String message) {
        this.message = message;
    }
}



