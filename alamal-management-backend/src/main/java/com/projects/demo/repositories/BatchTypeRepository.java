package com.projects.demo.repositories;

import com.projects.demo.entities.BatchType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchTypeRepository extends JpaRepository<BatchType, Long> {
    Optional<BatchType> findByType(String type);
    Optional<BatchType> findByTypeAndSection(String type, Long section);
}
