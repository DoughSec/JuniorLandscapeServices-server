package com.landscape.server.controller;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.model.dto.gallery.GalleryResponseDto;
import com.landscape.server.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/juniorLandscape/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GalleryResponseDto> getAll() {
        return galleryService.getAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GalleryResponseDto upload(
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String caption,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String filename,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) {
        if (url != null && !url.isBlank()) {
            return galleryService.saveFromPresign(url, caption, category, filename);
        }
        if (file != null && !file.isEmpty()) {
            return galleryService.uploadFile(file, caption, category);
        }
        throw new BadRequestException("Either 'url' or 'file' must be provided");
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("imageId") Integer imageId) {
        galleryService.delete(imageId);
    }
}
