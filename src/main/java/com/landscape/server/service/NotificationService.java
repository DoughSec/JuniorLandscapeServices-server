package com.landscape.server.service;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.exception.ResourceNotFoundException;
import com.landscape.server.model.Notification;
import com.landscape.server.model.NotificationStatus;
import com.landscape.server.model.dto.notification.NotificationResponseDto;
import com.landscape.server.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponseDto create(String firstName, String lastName, String email) {
        if (isBlank(firstName)) {
            throw new BadRequestException("firstName is required");
        }
        if (isBlank(lastName)) {
            throw new BadRequestException("lastName is required");
        }
        if (isBlank(email)) {
            throw new BadRequestException("email is required");
        }

        Notification notification = new Notification();
        notification.setFirstName(firstName);
        notification.setLastName(lastName);
        notification.setEmail(email);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setReviewCreated(false);

        notificationRepository.save(notification);
        return mapToResponse(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getAll() {
        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificationResponseDto getById(Integer notificationId) {
        return mapToResponse(getEntityById(notificationId));
    }

    public NotificationResponseDto approve(Integer notificationId) {
        Notification notification = getEntityById(notificationId);
        notification.setStatus(NotificationStatus.APPROVED);
        notificationRepository.save(notification);
        return mapToResponse(notification);
    }

    public NotificationResponseDto deny(Integer notificationId) {
        Notification notification = getEntityById(notificationId);
        notification.setStatus(NotificationStatus.DENIED);
        notificationRepository.save(notification);
        return mapToResponse(notification);
    }

    public Notification validateApprovedForReview(Integer notificationId) {
        Notification notification = getEntityById(notificationId);

        if (notification.getStatus() != NotificationStatus.APPROVED) {
            throw new BadRequestException("Notification request has not been approved");
        }
        if (notification.isReviewCreated()) {
            throw new BadRequestException("Notification request has already been used for a review");
        }

        return notification;
    }

    public void markReviewCreated(Notification notification) {
        notification.setReviewCreated(true);
        notificationRepository.save(notification);
    }

    private Notification getEntityById(Integer notificationId) {
        if (notificationId == null) {
            throw new BadRequestException("notificationId is required");
        }

        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));
    }

    private NotificationResponseDto mapToResponse(Notification notification) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setFirstName(notification.getFirstName());
        dto.setLastName(notification.getLastName());
        dto.setEmail(notification.getEmail());
        dto.setStatus(notification.getStatus());
        dto.setReviewCreated(notification.isReviewCreated());
        return dto;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
