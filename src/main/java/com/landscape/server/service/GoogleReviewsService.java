package com.landscape.server.service;

import com.landscape.server.model.dto.review.GoogleReviewResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GoogleReviewsService {

    // Google Places API returns at most 5 reviews per place
    private static final String PLACES_URL =
            "https://maps.googleapis.com/maps/api/place/details/json" +
            "?place_id={placeId}&fields=reviews&reviews_sort=newest&key={apiKey}";

    @Value("${google.places.api-key:}")
    private String apiKey;

    @Value("${google.places.place-id:}")
    private String placeId;

    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public List<GoogleReviewResponseDto> getReviews() {
        if (apiKey.isBlank() || placeId.isBlank()) {
            return List.of();
        }
        try {
            Map<String, Object> response = restTemplate.getForObject(
                    PLACES_URL, Map.class, Map.of("placeId", placeId, "apiKey", apiKey));

            if (response == null || !"OK".equals(response.get("status"))) {
                return List.of();
            }

            Map<String, Object> result = (Map<String, Object>) response.get("result");
            if (result == null) return List.of();

            List<Map<String, Object>> reviews = (List<Map<String, Object>>) result.get("reviews");
            if (reviews == null) return List.of();

            return reviews.stream().map(this::mapToDto).toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    private GoogleReviewResponseDto mapToDto(Map<String, Object> r) {
        GoogleReviewResponseDto dto = new GoogleReviewResponseDto();
        dto.setAuthorName((String) r.get("author_name"));
        dto.setRating(((Number) r.getOrDefault("rating", 0)).intValue());
        dto.setText((String) r.get("text"));
        dto.setRelativeTime((String) r.get("relative_time_description"));
        return dto;
    }
}
