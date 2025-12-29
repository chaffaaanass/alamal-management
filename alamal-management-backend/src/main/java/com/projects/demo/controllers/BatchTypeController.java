package com.projects.demo.controllers;

import com.projects.demo.dtos.BatchTypeDTO;
import com.projects.demo.services.BatchTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch-types")
public class BatchTypeController {
    private final BatchTypeService batchTypeService;

    public BatchTypeController(BatchTypeService batchTypeService) {
        this.batchTypeService = batchTypeService;
    }

    @GetMapping
    @Operation(summary = "Get batch types")
    public ResponseEntity<List<BatchTypeDTO>> getBatchTypes() {
        List<BatchTypeDTO> batchTypes = batchTypeService.getAllBatchTypes();
        return ResponseEntity.ok().body(batchTypes);
    }

    @GetMapping("/{type}")
    @Operation(summary = "Get batch type by type")
    public ResponseEntity<BatchTypeDTO> getBatchTypeByType(@PathVariable String type) {
        BatchTypeDTO batchTypeDTO = batchTypeService.getBatchTypeByType(type);
        return ResponseEntity.ok().body(batchTypeDTO);
    }

    @PostMapping
    @Operation(summary = "Create a new batch type")
    public ResponseEntity<BatchTypeDTO> createBatchType(@Validated({Default.class}) @RequestBody BatchTypeDTO batchTypeDTO) {
        BatchTypeDTO createdBatchType = batchTypeService.createBatchType(batchTypeDTO);
        return ResponseEntity.ok().body(createdBatchType);
    }

    @PutMapping("/{batchTypeId}")
    @Operation(summary = "Update an batch type")
    public ResponseEntity<BatchTypeDTO> updateBatchType(@PathVariable Long batchTypeId, @Validated({Default.class}) @RequestBody BatchTypeDTO batchTypeDTO) {
        BatchTypeDTO updatedBatchType = batchTypeService.updateBatchType(batchTypeId, batchTypeDTO);
        return ResponseEntity.ok().body(updatedBatchType);
    }

    @DeleteMapping("/{batchTypeId}")
    public ResponseEntity<Void> deleteBatchType(@PathVariable Long batchTypeId) {
        batchTypeService.deleteBatchType(batchTypeId);
        return ResponseEntity.noContent().build();
    }
}
