package com.landscape.server.model.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {
    private String firstName;
    private String lastName;
    private String email;
}
