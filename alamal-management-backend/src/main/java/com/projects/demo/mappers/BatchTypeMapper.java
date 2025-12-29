package com.projects.demo.mappers;


import com.projects.demo.dtos.BatchTypeDTO;
import com.projects.demo.entities.BatchType;

public class BatchTypeMapper {
    public static BatchTypeDTO toDTO(BatchType batchType) {
        BatchTypeDTO batchTypeDTO = new BatchTypeDTO();
        batchTypeDTO.setBatchTypeId(batchType.getBatchTypeId());
        batchTypeDTO.setType(batchType.getType());
        batchTypeDTO.setSection(batchType.getSection());

        return batchTypeDTO;
    }

    public static BatchType toModel(BatchTypeDTO batchTypeDTO) {
        BatchType batchType = new BatchType();
        batchType.setBatchTypeId(batchTypeDTO.getBatchTypeId());
        batchType.setType(batchTypeDTO.getType());
        batchType.setSection(batchTypeDTO.getSection());

        return batchType;
    }
}
