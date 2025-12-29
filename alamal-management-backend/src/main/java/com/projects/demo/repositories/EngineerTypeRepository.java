package com.projects.demo.repositories;

import com.projects.demo.entities.Engineer;
import com.projects.demo.entities.EngineerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EngineerTypeRepository extends JpaRepository<EngineerType, Long> {
    Optional<EngineerType> findByEngineerType(String engineerType);
}
