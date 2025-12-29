package com.projects.demo.controllers;

import com.projects.demo.dtos.EngineerTypeDTO;
import com.projects.demo.services.EngineerTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/engineer-types")
public class EngineerTypeController {
    private final EngineerTypeService engineerTypeService;

    public EngineerTypeController(EngineerTypeService engineerTypeService) {
        this.engineerTypeService = engineerTypeService;
    }

    @GetMapping
    @Operation(summary = "Get engineer types")
    public ResponseEntity<List<EngineerTypeDTO>> getEngineerTypes() {
        List<EngineerTypeDTO> engineerTypes = engineerTypeService.getAllEngineerTypes();
        return ResponseEntity.ok().body(engineerTypes);
    }

    @GetMapping("/{type}")
    @Operation(summary = "Get engineer type by type")
    public ResponseEntity<EngineerTypeDTO> getEngineerTypeByType(@PathVariable String type) {
        EngineerTypeDTO engineerTypeDTO = engineerTypeService.getEngineerTypeByType(type);
        return ResponseEntity.ok().body(engineerTypeDTO);
    }

    @PostMapping
    @Operation(summary = "Create a new engineer type")
    public ResponseEntity<EngineerTypeDTO> createEngineerType(@Validated({Default.class}) @RequestBody EngineerTypeDTO engineerTypeDTO) {
        EngineerTypeDTO createdEngineerType = engineerTypeService.createEngineerType(engineerTypeDTO);
        return ResponseEntity.ok().body(createdEngineerType);
    }

    @PutMapping("/{engineerTypeId}")
    @Operation(summary = "Update an engineer type")
    public ResponseEntity<EngineerTypeDTO> updateEngineerType(@PathVariable Long engineerTypeId, @Validated({Default.class}) @RequestBody EngineerTypeDTO engineerTypeDTO) {
        EngineerTypeDTO updatedEngineerType = engineerTypeService.updateEngineerType(engineerTypeId, engineerTypeDTO);
        return ResponseEntity.ok().body(updatedEngineerType);
    }

    @DeleteMapping("/{engineerTypeId}")
    public ResponseEntity<Void> deleteEngineerType(@PathVariable Long engineerTypeId) {
        engineerTypeService.deleteEngineerType(engineerTypeId);
        return ResponseEntity.noContent().build();
    }
}
