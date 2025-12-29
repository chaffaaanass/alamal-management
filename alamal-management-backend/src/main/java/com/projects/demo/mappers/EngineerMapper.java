package com.projects.demo.mappers;

import com.projects.demo.dtos.EngineerResponseDTO;
import com.projects.demo.entities.Engineer;


public class EngineerMapper {
    public static EngineerResponseDTO toDTO(Engineer engineer) {
        EngineerResponseDTO engineerResponseDTO = new EngineerResponseDTO();
        engineerResponseDTO.setEngineerId(engineer.getEngineerId());
        engineerResponseDTO.setChequeNumber(engineer.getChequeNumber());
        engineerResponseDTO.setEngineerName(engineer.getEngineerName());
        engineerResponseDTO.setAmount(engineer.getAmount());
        engineerResponseDTO.setChequeDate(engineer.getChequeDate().toString());
        engineerResponseDTO.setActs(engineer.getActs());
        engineerResponseDTO.setEngineerType(engineer.getEngineerType().getEngineerType());
        engineerResponseDTO.setCreatedBy(engineer.getCreatedBy().getUsername());

        return engineerResponseDTO;
    }
}
