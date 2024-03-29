package com.example.devohealthrecord.security;
import com.example.devohealthrecord.entities.AppUser;
import com.example.devohealthrecord.entities.Doctor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AppUserDetails implements UserDetails {
    private String email;
    private String fullName;
    private String password;
    private Boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public AppUserDetails(AppUser appUser) {
        this.email = appUser.getEmail();
        this.fullName = appUser.getFullName();
        this.password = appUser.getPassword();
        this.isEnabled = appUser.getIsVerified();
        this.authorities = Stream.of(new SimpleGrantedAuthority(appUser.getRole().name()))
                .collect(Collectors.toList());

    }

    public AppUserDetails(Doctor appUser) {
        this.email = appUser.getEmail();
        this.fullName = appUser.getFullName();
        this.password = appUser.getPassword();
        this.isEnabled = appUser.getIsVerified();
        this.authorities = Stream.of(new SimpleGrantedAuthority(appUser.getRole().name()))
                .collect(Collectors.toList());

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFullName() {
        return fullName;
    }
}
