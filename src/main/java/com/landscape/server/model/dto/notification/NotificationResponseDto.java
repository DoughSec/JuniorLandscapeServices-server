package com.landscape.server.model.dto.notification;

import com.landscape.server.model.NotificationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private NotificationStatus status;
    private boolean reviewCreated;
    private java.time.LocalDateTime createdAt;
}
