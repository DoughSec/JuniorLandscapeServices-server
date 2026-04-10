package com.landscape.server.model.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String email;
    private String password;
}