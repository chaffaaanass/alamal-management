package com.projects.demo.mappers;


import com.projects.demo.dtos.EngineerTypeDTO;
import com.projects.demo.entities.EngineerType;

import java.time.LocalDate;

public class EngineerTypeMapper {
    public static EngineerTypeDTO toDTO(EngineerType engineerType) {
        EngineerTypeDTO engineerTypeDTO = new EngineerTypeDTO();
        engineerTypeDTO.setEngineerTypeId(engineerType.getEngineerTypeId());
        engineerTypeDTO.setEngineerType(engineerType.getEngineerType());

        return engineerTypeDTO;
    }

    public static EngineerType toModel(EngineerTypeDTO engineerTypeDTO) {
        EngineerType engineerType = new EngineerType();
        engineerType.setEngineerTypeId(engineerTypeDTO.getEngineerTypeId());
        engineerType.setEngineerType(engineerTypeDTO.getEngineerType());

        return engineerType;
    }
}
