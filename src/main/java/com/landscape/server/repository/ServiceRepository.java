package com.landscape.server.repository;

import com.landscape.server.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
