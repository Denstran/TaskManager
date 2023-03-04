package com.example.taskmanager.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @Column(name = "username", unique = true, length = 25)
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 25)
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;

    @Column(name = "mail")
    @Size(max = 50)
    @NotBlank(message = "Email is mandatory")
    @Email
    private String mail;

}
