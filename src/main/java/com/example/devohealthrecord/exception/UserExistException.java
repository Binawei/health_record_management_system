package com.example.devohealthrecord.exception;

import org.springframework.http.HttpStatus;

public class UserExistException extends RuntimeException{
    public UserExistException(String message, HttpStatus badRequest) {
        super(message);
    }

    public UserExistException(String message) {
        super(message);
    }
}
