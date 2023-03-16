package com.example.taskmanager.controller;

import com.example.taskmanager.payload.request.LoginRequest;
import com.example.taskmanager.payload.request.SignupRequest;
import com.example.taskmanager.payload.request.TokenRefreshRequest;
import com.example.taskmanager.payload.response.JwtResponse;
import com.example.taskmanager.payload.response.TokenRefreshResponse;
import com.example.taskmanager.service.RefreshTokenService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.service.security.securityservice.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
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

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest){

        TokenRefreshResponse tokenRefreshResponse = refreshTokenService.refreshToken(tokenRefreshRequest);

        return new ResponseEntity<>(tokenRefreshResponse, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal UserDetailsImpl authUsr){
        Long userId = authUsr.getId();
        refreshTokenService.deleteByUserId(userId);

        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }
}
