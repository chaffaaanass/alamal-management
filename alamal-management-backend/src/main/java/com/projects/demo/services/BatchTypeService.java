package com.projects.demo.services;

import com.projects.demo.dtos.BatchTypeDTO;
import com.projects.demo.entities.BatchType;
import com.projects.demo.exceptions.BatchTypeNotFoundException;
import com.projects.demo.mappers.BatchTypeMapper;
import com.projects.demo.repositories.BatchTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchTypeService {
    private final BatchTypeRepository batchTypeRepository;

    public BatchTypeService(BatchTypeRepository batchTypeRepository) {
        this.batchTypeRepository = batchTypeRepository;
    }

    public List<BatchTypeDTO> getAllBatchTypes() {
        return batchTypeRepository.findAll().stream()
                .map(BatchTypeMapper::toDTO).toList();
    }

    public BatchTypeDTO getBatchTypeByType(String type) {
        BatchType batchType = batchTypeRepository.findByType(type)
                .orElseThrow(() -> new BatchTypeNotFoundException("Le lot introuvable."));

        return BatchTypeMapper.toDTO(batchType);
    }

    public BatchTypeDTO createBatchType(BatchTypeDTO batchTypeDTO) {
        BatchType newBatchType = batchTypeRepository.save(
                BatchTypeMapper.toModel(batchTypeDTO));

        return BatchTypeMapper.toDTO(newBatchType);
    }

    public BatchTypeDTO updateBatchType(Long batchTypeId, BatchTypeDTO batchTypeDTO) {
        BatchType updatedBatchType = batchTypeRepository.findById(batchTypeId)
                .orElseThrow(() -> new BatchTypeNotFoundException("Le lot introuvable avec son identifiant."));


        updatedBatchType.setType(batchTypeDTO.getType());
        updatedBatchType.setSection(batchTypeDTO.getSection());

        batchTypeRepository.save(updatedBatchType);
        return BatchTypeMapper.toDTO(updatedBatchType);
    }

    public void deleteBatchType(Long batchTypeId) {
        batchTypeRepository.deleteById(batchTypeId);
    }
}
