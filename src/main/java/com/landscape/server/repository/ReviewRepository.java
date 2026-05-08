package com.landscape.server.repository;

import com.landscape.server.model.Review;
import com.landscape.server.model.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByStatus(ReviewStatus status);
}
