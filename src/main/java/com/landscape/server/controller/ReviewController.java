package com.landscape.server.controller;

import com.landscape.server.model.dto.review.ReviewRequestDto;
import com.landscape.server.model.dto.review.ReviewResponseDto;
import com.landscape.server.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/juniorLandscape/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //create review record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto create(@RequestBody ReviewRequestDto request) {
        return reviewService.create(
                request.getNotificationId(),
                request.getRating(),
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
}
