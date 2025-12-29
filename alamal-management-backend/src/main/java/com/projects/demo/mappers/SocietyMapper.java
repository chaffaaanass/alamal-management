package com.projects.demo.mappers;


import com.projects.demo.dtos.SocietyResponseDTO;
import com.projects.demo.entities.Society;

public class SocietyMapper {
    public static SocietyResponseDTO toDTO(Society society) {
        SocietyResponseDTO societyResponseDTO = new SocietyResponseDTO();
        societyResponseDTO.setAttachmentNumber(society.getAttachmentNumber());
        societyResponseDTO.setChequeNumber(society.getChequeNumber());
        societyResponseDTO.setSocietyName(society.getSocietyName());
        societyResponseDTO.setDate(society.getDate().toString());
        societyResponseDTO.setSum(society.getSum());
        societyResponseDTO.setBatches(society.getBatches());
        societyResponseDTO.setBatchType(society.getBatchType().getType());
        societyResponseDTO.setSection(society.getBatchType().getSection());
        societyResponseDTO.setCreatedBy(society.getCreatedBy().getUsername());

        return societyResponseDTO;
    }
}
