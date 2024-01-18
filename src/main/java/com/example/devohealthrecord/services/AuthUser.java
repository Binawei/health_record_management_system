package com.example.devohealthrecord.services;


import com.example.devohealthrecord.enums.Role;

public interface AuthUser {
    String getEmail();
    String getPassword();
    Role getRole();
}
