package com.landscape.server.service;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.model.Notification;
import com.landscape.server.model.Review;
import com.landscape.server.model.dto.review.ReviewResponseDto;
import com.landscape.server.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final NotificationService notificationService;

    public ReviewService(
            ReviewRepository reviewRepository,
            NotificationService notificationService
    ) {
        this.reviewRepository = reviewRepository;
        this.notificationService = notificationService;
    }

    // create Review
    public ReviewResponseDto create(
            Integer notificationId, double rating, String comment, String firstName, String lastName
    ) {
        if (rating < 0 || rating > 5) {
            throw new BadRequestException("rating must be between 0 and 5");
        }

        Notification notification = notificationService.validateApprovedForReview(notificationId);

        Review review = new Review();
        review.setNotificationId(notificationId);
        review.setRating(rating);
        review.setComment(comment);
        review.setFirstName(firstName);
        review.setLastName(lastName);

        reviewRepository.save(review);
        notificationService.markReviewCreated(notification);

        ReviewResponseDto responseDto = new ReviewResponseDto();
        responseDto.setId(review.getId());
        responseDto.setNotificationId(review.getNotificationId());
        responseDto.setRating(rating);
        responseDto.setComment(comment);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(review -> {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setId(review.getId());
            dto.setNotificationId(review.getNotificationId());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setFirstName(review.getFirstName());
            dto.setLastName(review.getLastName());
            return dto;
        }).toList();
    }
}
