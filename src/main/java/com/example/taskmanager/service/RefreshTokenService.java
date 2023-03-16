package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.ResourceNotFoundException;
import com.example.taskmanager.exceptions.TokenRefreshException;
import com.example.taskmanager.model.RefreshToken;
import com.example.taskmanager.model.User;
import com.example.taskmanager.payload.request.TokenRefreshRequest;
import com.example.taskmanager.payload.response.TokenRefreshResponse;
import com.example.taskmanager.repository.RefreshTokenRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public RefreshToken createRefreshToken(Long usrId) {
        RefreshToken refreshToken = new RefreshToken();

        User user = userRepository.findById(usrId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + usrId));

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. " +
                    "Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long usrId) {
        User user = userRepository.findById(usrId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + usrId));
        return refreshTokenRepository.deleteByUser(user);
    }
}
