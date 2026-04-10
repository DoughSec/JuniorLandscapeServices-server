package com.landscape.server.service;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.exception.ResourceNotFoundException;
import com.landscape.server.model.User;
import com.landscape.server.model.dto.auth.LoginResponseDto;
import com.landscape.server.model.dto.auth.UserMeResponse;
import com.landscape.server.repository.UserRepository;
import com.landscape.server.security.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils JwtUtils;

    // 60 minutes
    private static final long TTL_MILLIS = 60L * 60L * 1000L;

    public AuthService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder, JwtUtils JwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.JwtUtils = JwtUtils;
    }

    // user login
    public LoginResponseDto login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = JwtUtils.createJWT(
                String.valueOf(user.getId()), // token id
                "nomadTrack",                  // issuer
                user.getEmail(),               // subject
                TTL_MILLIS,
                user.getRole()                 // role claim
        );

        return new LoginResponseDto(token, TTL_MILLIS / 1000);
    }

    // get current logged in user
    public UserMeResponse me(String token) {

        Claims claims = JwtUtils.decodeJWT(token);

        // subject = email (from how we created the token)
        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserMeResponse(
                user.getId(),
                user.getEmail()
        );
    }

    public String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Missing or invalid Authorization header");
        }
        return authorizationHeader.substring(7);
    }
}