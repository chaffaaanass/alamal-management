package com.projects.demo.controllers;

import com.projects.demo.dtos.SocietyRequestDTO;
import com.projects.demo.dtos.SocietyResponseDTO;
import com.projects.demo.dtos.SocietySummary;
import com.projects.demo.services.SocietyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/societies")
public class SocietyController {
    private final SocietyService societyService;

    public SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @GetMapping
    @Operation(summary = "Get societies")
    public ResponseEntity<List<SocietyResponseDTO>> getSocieties() {
        List<SocietyResponseDTO> societies = societyService.getAllSocieties();
        return ResponseEntity.ok().body(societies);
    }

    @GetMapping("/summary/{societyName}")
    public SocietySummary getSocietySummary(@PathVariable String societyName) {
        return societyService.buildSocietySummary(societyName);
    }

    @GetMapping("/{societyName}")
    @Operation(summary = "Get societies by their society name")
    public ResponseEntity<List<SocietyResponseDTO>> getSocietiesBySocietyName(@PathVariable String societyName) {
        List<SocietyResponseDTO> societies = societyService.getAllSocietiesBySocietyName(societyName);
        return ResponseEntity.ok().body(societies);
    }

    @GetMapping("/{batchType}")
    @Operation(summary = "Get societies by batch type")
    public ResponseEntity<List<SocietyResponseDTO>> getSocietiesByBatchType(@PathVariable String batchType) {
        List<SocietyResponseDTO> societies = societyService.getAllSocietiesByBatchType(batchType);
        return ResponseEntity.ok().body(societies);
    }

    @GetMapping("/{societyName}/{date}")
    @Operation(summary = "Get societies by attachment number and date")
    public ResponseEntity<List<SocietyResponseDTO>> getSocietiesBySocietyNameAndDate(@PathVariable String societyName, @PathVariable LocalDate date) {
        List<SocietyResponseDTO> societies = societyService.getAllSocietiesBySocietyNameAndDate(societyName, date);
        return ResponseEntity.ok().body(societies);
    }

    @GetMapping("/{date}")
    @Operation(summary = "Get societies by date")
    public ResponseEntity<List<SocietyResponseDTO>> getSocietiesByDate(@PathVariable LocalDate date) {
        List<SocietyResponseDTO> societies = societyService.getAllSocietiesByDate(date);
        return ResponseEntity.ok().body(societies);
    }

    @GetMapping("/{chequeNumber}")
    @Operation(summary = "Get society by cheque number")
    public ResponseEntity<SocietyResponseDTO> getSocietyByChequeNumber(@PathVariable Long chequeNumber) {
        SocietyResponseDTO society = societyService.getSocietyByChequeNumber(chequeNumber);
        return ResponseEntity.ok().body(society);
    }

    @PostMapping
    @Operation(summary = "Create a new society")
    public ResponseEntity<SocietyResponseDTO> createSociety(@Validated({Default.class}) @RequestBody SocietyRequestDTO societyRequestDTO) {
        SocietyResponseDTO createdSociety = societyService.createSociety(societyRequestDTO);
        return ResponseEntity.ok().body(createdSociety);
    }

    @PutMapping("/{societyId}")
    @Operation(summary = "Create a new society")
    public ResponseEntity<SocietyResponseDTO> updateSociety(@PathVariable Long societyId, @Validated({Default.class}) @RequestBody SocietyRequestDTO societyRequestDTO) {
        SocietyResponseDTO createdSociety = societyService.updateSociety(societyId, societyRequestDTO);
        return ResponseEntity.ok().body(createdSociety);
    }

    @DeleteMapping("/{societyId}")
    public ResponseEntity<Void> deleteSociety(@PathVariable Long societyId) {
        societyService.deleteSociety(societyId);
        return ResponseEntity.noContent().build();
    }
}
