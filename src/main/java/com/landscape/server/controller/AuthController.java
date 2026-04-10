package com.landscape.server.controller;

import com.landscape.server.model.User;
import com.landscape.server.model.dto.auth.LoginRequestDto;
import com.landscape.server.model.dto.auth.LoginResponseDto;
import com.landscape.server.model.dto.auth.RegisterRequestDto;
import com.landscape.server.model.dto.auth.UserMeResponse;
import com.landscape.server.service.AuthService;
import com.landscape.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/juniorLandscape/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegisterRequestDto request) {
        return userService.register(
                request.getEmail(),
                request.getPassword()
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserMeResponse me(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authService.extractBearerToken(authorizationHeader);
        return authService.me(token);
    }
}
