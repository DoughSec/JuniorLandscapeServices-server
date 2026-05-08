package com.landscape.server.controller;

import com.landscape.server.model.dto.notification.NotificationRequestDto;
import com.landscape.server.model.dto.notification.NotificationResponseDto;
import com.landscape.server.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juniorLandscape/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponseDto create(@RequestBody NotificationRequestDto request) {
        return notificationService.create(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getMessage()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<NotificationResponseDto> getAllNotifications() {
        return notificationService.getAll();
    }

    @GetMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponseDto getNotification(@PathVariable("notificationId") Integer notificationId) {
        return notificationService.getById(notificationId);
    }

    @PutMapping("/{notificationId}/approve")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public NotificationResponseDto approve(@PathVariable("notificationId") Integer notificationId) {
        return notificationService.approve(notificationId);
    }

    @PutMapping("/{notificationId}/deny")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public NotificationResponseDto deny(@PathVariable("notificationId") Integer notificationId) {
        return notificationService.deny(notificationId);
    }
}
