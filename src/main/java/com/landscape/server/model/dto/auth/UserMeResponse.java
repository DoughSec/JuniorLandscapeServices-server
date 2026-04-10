package com.landscape.server.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {

    private Integer id;
    private String email;

}