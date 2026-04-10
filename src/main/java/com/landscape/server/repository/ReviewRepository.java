package com.landscape.server.repository;

import com.landscape.server.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
