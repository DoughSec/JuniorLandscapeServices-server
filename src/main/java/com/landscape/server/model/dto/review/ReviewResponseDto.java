package com.landscape.server.model.dto.review;

import com.landscape.server.model.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Integer id;
    private Integer notificationId;
    private ReviewStatus status;
    private double rating;
    private String title;
    private String comment;
    private String firstName;
    private String lastName;
}
