package com.example.taskmanager.controller;

import com.example.taskmanager.payload.request.LoginRequest;
import com.example.taskmanager.payload.request.SignupRequest;
import com.example.taskmanager.payload.response.JwtResponse;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        userService.saveUser(signupRequest);

        return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.signIn(loginRequest);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
