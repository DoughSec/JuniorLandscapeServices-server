package com.landscape.server.repository;

import com.landscape.server.model.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<GalleryImage, Integer> {
}
