package com.landscape.server.controller;

import com.landscape.server.model.dto.review.GoogleReviewResponseDto;
import com.landscape.server.model.dto.review.ReviewRequestDto;
import com.landscape.server.model.dto.review.ReviewResponseDto;
import com.landscape.server.service.GoogleReviewsService;
import com.landscape.server.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juniorLandscape/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final GoogleReviewsService googleReviewsService;

    public ReviewController(ReviewService reviewService, GoogleReviewsService googleReviewsService) {
        this.reviewService = reviewService;
        this.googleReviewsService = googleReviewsService;
    }

    //create review record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@RequestBody ReviewRequestDto request) {
        return reviewService.create(
                request.getStatus(),
                request.getRating(),
                request.getTitle(),
                request.getComment(),
                request.getFirstName(),
                request.getLastName()
        );
    }

    //get all reviews
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAll();
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getPendingReviews() {
        return reviewService.getPending();
    }

    @GetMapping("/approved")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getApprovedReviews() {
        return reviewService.getApproved();
    }

    @GetMapping("/denied")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getDeniedReviews() {
        return reviewService.getDenied();
    }

    @PutMapping("/{reviewId}/approve")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReviewResponseDto approve(@PathVariable("reviewId") Integer reviewId) {
        return reviewService.approve(reviewId);
    }

    @PutMapping("/{reviewId}/deny")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReviewResponseDto deny(@PathVariable("reviewId") Integer reviewId) {
        return reviewService.deny(reviewId);
    }

    @PutMapping("/approve-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ReviewResponseDto> approveAll() {
        return reviewService.approveAll();
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("reviewId") Integer reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping("/google")
    @ResponseStatus(HttpStatus.OK)
    public List<GoogleReviewResponseDto> getGoogleReviews() {
        return googleReviewsService.getReviews();
    }
}
