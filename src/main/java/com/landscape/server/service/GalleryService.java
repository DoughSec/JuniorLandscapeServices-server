package com.landscape.server.service;

import com.landscape.server.exception.BadRequestException;
import com.landscape.server.exception.ResourceNotFoundException;
import com.landscape.server.model.GalleryImage;
import com.landscape.server.model.dto.gallery.GalleryResponseDto;
import com.landscape.server.repository.GalleryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GalleryService {

    private static final String BUCKET_NAME = "juniorlandscape-images";
    private static final String BUCKET_URL_PREFIX = "https://" + BUCKET_NAME + ".s3.us-east-2.amazonaws.com/";
    private static final String KEY_PREFIX = "listings/";

    private final GalleryRepository galleryRepository;
    private final S3Client s3Client;

    public GalleryService(GalleryRepository galleryRepository, S3Client s3Client) {
        this.galleryRepository = galleryRepository;
        this.s3Client = s3Client;
    }

    @Transactional(readOnly = true)
    public List<GalleryResponseDto> getAll() {
        return galleryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Presign path: image already in S3, just persist the URL and metadata
    public GalleryResponseDto saveFromPresign(String url, String caption, String category, String filename) {
        GalleryImage image = new GalleryImage();
        image.setUrl(url);
        image.setCaption(caption);
        image.setCategory(category != null ? category : "GENERAL");
        image.setFilename(filename);
        galleryRepository.save(image);
        return mapToResponse(image);
    }

    // Fallback path: upload the file to S3 from the backend, then persist
    public GalleryResponseDto uploadFile(MultipartFile file, String caption, String category) {
        String key = KEY_PREFIX + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            throw new BadRequestException("Failed to read uploaded file");
        }

        GalleryImage image = new GalleryImage();
        image.setUrl(BUCKET_URL_PREFIX + key);
        image.setCaption(caption);
        image.setCategory(category != null ? category : "GENERAL");
        image.setFilename(file.getOriginalFilename());
        galleryRepository.save(image);
        return mapToResponse(image);
    }

    public void delete(Integer id) {
        GalleryImage image = galleryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery image not found: " + id));

        // Best-effort S3 delete — don't fail if the key is missing or malformed
        try {
            String url = image.getUrl();
            if (url != null && url.startsWith(BUCKET_URL_PREFIX)) {
                String key = url.substring(BUCKET_URL_PREFIX.length());
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(key)
                        .build());
            }
        } catch (Exception ignored) {}

        galleryRepository.delete(image);
    }

    private GalleryResponseDto mapToResponse(GalleryImage image) {
        GalleryResponseDto dto = new GalleryResponseDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setCaption(image.getCaption());
        dto.setFilename(image.getFilename());
        dto.setCategory(image.getCategory());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }
}
