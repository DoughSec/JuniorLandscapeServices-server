package com.landscape.server.model.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
    private Integer id;
    private Integer notificationId;
    private double rating;
    private String comment;
    private String firstName;
    private String lastName;
}
