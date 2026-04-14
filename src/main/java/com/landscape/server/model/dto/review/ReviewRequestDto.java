package com.landscape.server.model.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private double rating;
    private String title;
    private String comment;
    private String firstName;
    private String lastName;
}
