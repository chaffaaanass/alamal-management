package com.projects.demo.controllers;

import com.projects.demo.dtos.EngineerRequestDTO;
import com.projects.demo.dtos.EngineerResponseDTO;
import com.projects.demo.dtos.EngineerSummary;
import com.projects.demo.services.EngineerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/engineers")
public class EngineerController {
    private final EngineerService engineerService;

    public EngineerController(EngineerService engineerService) {
        this.engineerService = engineerService;
    }

    @GetMapping
    @Operation(summary = "Get engineers")
    public ResponseEntity<List<EngineerResponseDTO>> getEngineers() {
        List<EngineerResponseDTO> engineers = engineerService.getAllEngineers();
        return ResponseEntity.ok().body(engineers);
    }

    @GetMapping("/summary/{name}")
    public EngineerSummary getEngineerSummary(@PathVariable String name) {
        return engineerService.buildEngineerSummary(name);
    }

    @GetMapping("/by-name/{engineerName}")
    @Operation(summary = "Get engineers by their name")
    public ResponseEntity<List<EngineerResponseDTO>> getEngineersByName(@PathVariable String engineerName) {
        List<EngineerResponseDTO> engineers = engineerService.getAllEngineersByEngineerName(engineerName);
        return ResponseEntity.ok().body(engineers);
    }

    @GetMapping("/by-type/{engineerType}")
    @Operation(summary = "Get engineers by their type")
    public ResponseEntity<List<EngineerResponseDTO>> getEngineersByType(@PathVariable String engineerType) {
        List<EngineerResponseDTO> engineers = engineerService.getAllEngineersByEngineerType(engineerType);
        return ResponseEntity.ok().body(engineers);
    }

    @GetMapping("/{engineerName}/{chequeDate}")
    @Operation(summary = "Get engineers by their name and cheque")
    public ResponseEntity<List<EngineerResponseDTO>> getEngineersByNameAndChequeDate(@PathVariable String engineerName, @PathVariable LocalDate chequeDate) {
        List<EngineerResponseDTO> engineers = engineerService.getAllEngineersByEngineerNameAndChequeDate(engineerName, chequeDate);
        return ResponseEntity.ok().body(engineers);
    }

    @GetMapping("/all/{chequeDate}")
    @Operation(summary = "Get engineers by their cheque")
    public ResponseEntity<List<EngineerResponseDTO>> getEngineersByChequeDate(@PathVariable LocalDate chequeDate) {
        List<EngineerResponseDTO> engineers = engineerService.getAllEngineersByChequeDate(chequeDate);
        return ResponseEntity.ok().body(engineers);
    }

    @GetMapping("/{chequeDate}")
    @Operation(summary = "Get engineer type by type")
    public ResponseEntity<EngineerResponseDTO> getEngineerByChequeNumber(@PathVariable Long chequeDate) {
        EngineerResponseDTO engineer = engineerService.getEngineerByChequeNumber(chequeDate);
        return ResponseEntity.ok().body(engineer);
    }

    @PostMapping
    @Operation(summary = "Create a new engineer")
    public ResponseEntity<EngineerResponseDTO> createEngineer(@Validated({Default.class}) @RequestBody EngineerRequestDTO engineerRequestDTO) {
        EngineerResponseDTO createdEngineer = engineerService.createEngineer(engineerRequestDTO);
        return ResponseEntity.ok().body(createdEngineer);
    }

    @PutMapping("/{engineerId}")
    @Operation(summary = "Update an engineer")
    public ResponseEntity<EngineerResponseDTO> updateEngineer(@PathVariable Long engineerId, @Validated({Default.class}) @RequestBody EngineerRequestDTO engineerRequestDTO) {
        EngineerResponseDTO updatedEngineer = engineerService.updateEngineer(engineerId, engineerRequestDTO);
        return ResponseEntity.ok().body(updatedEngineer);
    }

    @DeleteMapping("/{engineerId}")
    public ResponseEntity<Void> deleteEngineer(@PathVariable Long engineerId) {
        engineerService.deleteEngineer(engineerId);
        return ResponseEntity.noContent().build();
    }
}
