package com.landscape.server.model.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleReviewResponseDto {
    private String authorName;
    private int rating;
    private String text;
    private String relativeTime;
}
