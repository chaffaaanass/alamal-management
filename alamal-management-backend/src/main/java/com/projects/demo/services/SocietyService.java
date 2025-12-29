package com.projects.demo.services;

import com.projects.demo.dtos.SocietyRequestDTO;
import com.projects.demo.dtos.SocietyResponseDTO;
import com.projects.demo.dtos.SocietySummary;
import com.projects.demo.entities.BatchType;
import com.projects.demo.entities.Engineer;
import com.projects.demo.entities.Society;
import com.projects.demo.entities.User;
import com.projects.demo.exceptions.BatchTypeNotFoundException;
import com.projects.demo.exceptions.ChequeAlreadyExistsException;
import com.projects.demo.exceptions.SocietyNotFoundException;
import com.projects.demo.exceptions.UserNotFoundException;
import com.projects.demo.mappers.SocietyMapper;
import com.projects.demo.repositories.BatchTypeRepository;
import com.projects.demo.repositories.SocietyRepository;
import com.projects.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SocietyService {
    private final SocietyRepository societyRepository;
    private final BatchTypeRepository batchTypeRepository;
    private final UserRepository userRepository;

    public SocietyService(SocietyRepository societyRepository, BatchTypeRepository batchTypeRepository, UserRepository userRepository) {
        this.societyRepository = societyRepository;
        this.batchTypeRepository = batchTypeRepository;
        this.userRepository = userRepository;
    }

    public List<SocietyResponseDTO> getAllSocieties() {
        return societyRepository.findAll().stream()
                .map(SocietyMapper::toDTO).toList();
    }

    public double sumSums(List<Society> societies) {
        return societies.stream()
                .map(Society::getSum)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double sumBatches(List<Double> batches) {
        return batches == null ? 0.0 :
                batches.stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .sum();
    }


    public SocietySummary buildSocietySummary(String societyName) {
        List<Society> societies =
                societyRepository.findAllBySocietyName(societyName);

        SocietySummary dto = new SocietySummary();
        dto.setSocietyName(societyName);

        List<Double> allBatches = societies.stream()
                .map(Society::getBatches)          // List<List<Double>>
                .filter(Objects::nonNull)
                .flatMap(List::stream)           // List<Double>
                .filter(Objects::nonNull)
                .toList();

        dto.setBatches(allBatches);
        dto.setTotalBatches(sumBatches(allBatches));
        dto.setTotalSum(sumSums(societies));

        return dto;
    }

    public List<SocietyResponseDTO> getAllSocietiesBySocietyName(String societyName) {
        return societyRepository.findAllBySocietyName(societyName).stream()
                .map(SocietyMapper::toDTO).toList();
    }

    public List<SocietyResponseDTO> getAllSocietiesByBatchType(String type) {
        return societyRepository.findAllByBatchTypeType(type).stream()
                .map(SocietyMapper::toDTO).toList();
    }

    public List<SocietyResponseDTO> getAllSocietiesBySocietyNameAndDate(String societyName, LocalDate date) {
        return societyRepository.findAllBySocietyNameAndDate(societyName, date).stream()
                .map(SocietyMapper::toDTO).toList();
    }

    public List<SocietyResponseDTO> getAllSocietiesByDate(LocalDate date) {
        return societyRepository.findAllByDate(date).stream()
                .map(SocietyMapper::toDTO).toList();
    }

    public SocietyResponseDTO getSocietyByChequeNumber(Long chequeNumber) {
        Society society = societyRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new SocietyNotFoundException("La sociéte introuvable avec ce numéro de chèque."));

        return SocietyMapper.toDTO(society);
    }

    public SocietyResponseDTO createSociety(SocietyRequestDTO societyRequestDTO) {
        if (societyRepository.existsByChequeNumber(societyRequestDTO.getChequeNumber())) {
            throw new ChequeAlreadyExistsException("La sociéte possédant ce numéro de chèque "
                    + societyRequestDTO.getChequeNumber() + "existe déjà.");
        }

        Society newSociety = new Society();

        if(societyRequestDTO.getSection() > 0) {
            BatchType batchType = batchTypeRepository.findByTypeAndSection(societyRequestDTO.getBatchType(), societyRequestDTO.getSection())
                    .orElseThrow(() -> new BatchTypeNotFoundException("le type de lot est introuvable."));
            newSociety.setBatchType(batchType);
        }
        else {
            BatchType batchType = batchTypeRepository.findByType(societyRequestDTO.getBatchType())
                    .orElseThrow(() -> new BatchTypeNotFoundException("le type de lot est introuvable."));
            newSociety.setBatchType(batchType);
        }


        User createdBy = userRepository.findById(societyRequestDTO.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException("L'utilisateur est introuvable."));


        newSociety.setChequeNumber(societyRequestDTO.getChequeNumber());
        newSociety.setSocietyName(societyRequestDTO.getSocietyName());
        newSociety.setDate(LocalDate.parse(societyRequestDTO.getDate()));
        newSociety.setSum(societyRequestDTO.getSum());
        newSociety.setBatches(societyRequestDTO.getBatches());
        newSociety.getBatchType().setSection(societyRequestDTO.getSection());
        newSociety.setCreatedBy(createdBy);

        societyRepository.save(newSociety);
        return SocietyMapper.toDTO(newSociety);
    }

    public SocietyResponseDTO updateSociety(Long societyId, SocietyRequestDTO societyRequestDTO) {
        Society updatedSociety = societyRepository.findById(societyId)
                .orElseThrow(() -> new SocietyNotFoundException("La sociéte introuvable avec sa identifiant: " + societyId));

        if(societyRequestDTO.getSection() > 0) {
            BatchType batchType = batchTypeRepository.findByTypeAndSection(societyRequestDTO.getBatchType(), societyRequestDTO.getSection())
                    .orElseThrow(() -> new BatchTypeNotFoundException("le type de lot est introuvable."));
            updatedSociety.setBatchType(batchType);
        }
        else {
            BatchType batchType = batchTypeRepository.findByType(societyRequestDTO.getBatchType())
                    .orElseThrow(() -> new BatchTypeNotFoundException("le type de lot est introuvable."));
            updatedSociety.setBatchType(batchType);
        }


        updatedSociety.setChequeNumber(societyRequestDTO.getChequeNumber());
        updatedSociety.setSocietyName(societyRequestDTO.getSocietyName());
        updatedSociety.setDate(LocalDate.parse(societyRequestDTO.getDate()));
        updatedSociety.setSum(societyRequestDTO.getSum());
        updatedSociety.setBatches(societyRequestDTO.getBatches());

        societyRepository.save(updatedSociety);
        return SocietyMapper.toDTO(updatedSociety);
    }

    public void deleteSociety(Long societyId) {
        societyRepository.deleteById(societyId);
    }
}
