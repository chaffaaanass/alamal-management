package com.projects.demo.services;

import com.projects.demo.dtos.EngineerTypeDTO;
import com.projects.demo.entities.EngineerType;
import com.projects.demo.exceptions.EngineerTypeNotFoundException;
import com.projects.demo.mappers.EngineerTypeMapper;
import com.projects.demo.repositories.EngineerTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineerTypeService {
    private final EngineerTypeRepository engineerTypeRepository;

    public EngineerTypeService(EngineerTypeRepository engineerTypeRepository) {
        this.engineerTypeRepository = engineerTypeRepository;
    }

    public List<EngineerTypeDTO> getAllEngineerTypes() {
        return engineerTypeRepository.findAll().stream()
                .map(EngineerTypeMapper::toDTO).toList();
    }

    public EngineerTypeDTO getEngineerTypeByType(String type) {
        EngineerType engineerType = engineerTypeRepository.findByEngineerType(type)
                .orElseThrow(() -> new EngineerTypeNotFoundException("Le type de l'ingénieur introuvable."));

        return EngineerTypeMapper.toDTO(engineerType);
    }

    public EngineerTypeDTO createEngineerType(EngineerTypeDTO engineerTypeDTO) {
        EngineerType newEngineerType = engineerTypeRepository.save(
            EngineerTypeMapper.toModel(engineerTypeDTO));

        return EngineerTypeMapper.toDTO(newEngineerType);
    }

    public EngineerTypeDTO updateEngineerType(Long engineerTypeId, EngineerTypeDTO engineerTypeDTO) {
        EngineerType updatedEngineerType = engineerTypeRepository.findById(engineerTypeId)
                .orElseThrow(() -> new EngineerTypeNotFoundException("L'ingénieur introuvable avec son identifiant."));


        updatedEngineerType.setEngineerType(engineerTypeDTO.getEngineerType());

        engineerTypeRepository.save(updatedEngineerType);
        return EngineerTypeMapper.toDTO(updatedEngineerType);
    }

    public void deleteEngineerType(Long engineerTypeId) {
        engineerTypeRepository.deleteById(engineerTypeId);
    }
}
