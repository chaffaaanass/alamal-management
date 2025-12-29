package com.projects.demo.repositories;

import com.projects.demo.entities.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SocietyRepository extends JpaRepository<Society, Long> {
    List<Society> findAllBySocietyName(String societyName);
    List<Society> findAllByBatchTypeType(String type);
    List<Society> findAllBySocietyNameAndDate(String societyName, LocalDate date);
    List<Society> findAllByDate(LocalDate date);
    Optional<Society> findByChequeNumber(Long chequeNumber);
    boolean existsByChequeNumber(Long chequeNumber);
}
