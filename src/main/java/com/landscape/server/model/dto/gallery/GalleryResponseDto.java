package com.landscape.server.model.dto.gallery;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GalleryResponseDto {
    private Integer id;
    private String url;
    private String caption;
    private String filename;
    private String category;
    private LocalDateTime createdAt;
}
