package com.projects.demo.repositories;

import com.projects.demo.entities.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EngineerRepository extends JpaRepository<Engineer, Long> {
    List<Engineer> findAllByEngineerName(String engineerName);
    Optional<Engineer> findByChequeNumber(Long chequeNumber);
    List<Engineer> findAllByEngineerTypeEngineerType(String engineerType);
    List<Engineer> findAllByEngineerNameAndChequeDate(String engineerName, LocalDate chequeDate);
    List<Engineer> findAllByChequeDate(LocalDate chequeDate);
    boolean existsByChequeNumber(Long chequeNumber);
}
