package com.projects.demo.services;

import com.projects.demo.dtos.EngineerRequestDTO;
import com.projects.demo.dtos.EngineerResponseDTO;
import com.projects.demo.dtos.EngineerSummary;
import com.projects.demo.entities.Engineer;
import com.projects.demo.entities.EngineerType;
import com.projects.demo.entities.User;
import com.projects.demo.exceptions.ChequeAlreadyExistsException;
import com.projects.demo.exceptions.EngineerNotFoundException;
import com.projects.demo.exceptions.EngineerTypeNotFoundException;
import com.projects.demo.exceptions.UserNotFoundException;
import com.projects.demo.mappers.EngineerMapper;
import com.projects.demo.repositories.EngineerRepository;
import com.projects.demo.repositories.EngineerTypeRepository;
import com.projects.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class EngineerService {
    private final EngineerRepository engineerRepository;
    private final EngineerTypeRepository engineerTypeRepository;
    private final UserRepository userRepository;

    public EngineerService(EngineerRepository engineerRepository, EngineerTypeRepository engineerTypeRepository, UserRepository userRepository) {
        this.engineerRepository = engineerRepository;
        this.engineerTypeRepository = engineerTypeRepository;
        this.userRepository = userRepository;
    }

    public List<EngineerResponseDTO> getAllEngineers() {
        return engineerRepository.findAll().stream()
                .map(EngineerMapper::toDTO).toList();
    }

    public List<EngineerResponseDTO> getAllEngineersByEngineerName(String engineerName) {
        return engineerRepository.findAllByEngineerName(engineerName).stream()
                .map(EngineerMapper::toDTO)
                .toList();
    }

    public List<EngineerResponseDTO> getAllEngineersByEngineerType(String engineerType) {
        return engineerRepository.findAllByEngineerTypeEngineerType(engineerType).stream()
                .map(EngineerMapper::toDTO)
                .toList();
    }

    public List<EngineerResponseDTO> getAllEngineersByEngineerNameAndChequeDate(String engineerName, LocalDate chequeDate) {
        return engineerRepository.findAllByEngineerNameAndChequeDate(engineerName,chequeDate).stream()
                .map(EngineerMapper::toDTO)
                .toList();
    }

    public List<EngineerResponseDTO> getAllEngineersByChequeDate(LocalDate chequeDate) {
        return engineerRepository.findAllByChequeDate(chequeDate).stream()
                .map(EngineerMapper::toDTO)
                .toList();
    }

    public EngineerResponseDTO getEngineerByChequeNumber(Long chequeNumber) {
        Engineer engineer = engineerRepository.findByChequeNumber(chequeNumber)
                .orElseThrow(() -> new EngineerNotFoundException("L'ingénieur introuvable avec ce numéro de chèque."));

        return EngineerMapper.toDTO(engineer);
    }

    public EngineerResponseDTO createEngineer(EngineerRequestDTO engineerRequestDTO) {
        if(engineerRepository.existsByChequeNumber(engineerRequestDTO.getChequeNumber())) {
            throw new ChequeAlreadyExistsException("L'ingénieur possédant ce numéro de chèque "
                    + engineerRequestDTO.getChequeNumber() + "existe déjà.");

        }
        EngineerType engineerType = engineerTypeRepository.findByEngineerType(engineerRequestDTO.getEngineerType())
                .orElseThrow(() -> new EngineerTypeNotFoundException("Le type de l'ingénieur introuvable."));

        User createdBy = userRepository.findById(engineerRequestDTO.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException("L'utilisateur est introuvable."));

        Engineer newEngineer = new Engineer();

        newEngineer.setChequeNumber(engineerRequestDTO.getChequeNumber());
        newEngineer.setEngineerName(engineerRequestDTO.getEngineerName());
        newEngineer.setAmount(engineerRequestDTO.getAmount());
        newEngineer.setChequeDate(LocalDate.parse(engineerRequestDTO.getChequeDate()));
        newEngineer.setActs(engineerRequestDTO.getActs());
        newEngineer.setEngineerType(engineerType);
        newEngineer.setCreatedBy(createdBy);

        engineerRepository.save(newEngineer);

        return EngineerMapper.toDTO(newEngineer);
    }

    public EngineerResponseDTO updateEngineer(Long engineerId, EngineerRequestDTO engineerRequestDTO) {
        Engineer updatedEngineer = engineerRepository.findById(engineerId)
                .orElseThrow(() -> new EngineerNotFoundException("L'ingénieur introuvable avec son identifiant: " + engineerId));

        EngineerType engineerType = engineerTypeRepository.findByEngineerType(engineerRequestDTO.getEngineerType())
                .orElseThrow(() -> new EngineerTypeNotFoundException("Le type de l'ingénieur introuvable."));

        updatedEngineer.setChequeNumber(engineerRequestDTO.getChequeNumber());
        updatedEngineer.setEngineerName(engineerRequestDTO.getEngineerName());
        updatedEngineer.setAmount(engineerRequestDTO.getAmount());
        updatedEngineer.setChequeDate(LocalDate.parse(engineerRequestDTO.getChequeDate()));
        updatedEngineer.setActs(engineerRequestDTO.getActs());
        updatedEngineer.setEngineerType(engineerType);

        engineerRepository.save(updatedEngineer);
        return EngineerMapper.toDTO(updatedEngineer);
    }

    public void deleteEngineer(Long engineerId) { engineerRepository.deleteById(engineerId); }

    public double sumAmounts(List<Engineer> engineers) {
        return engineers.stream()
                .map(Engineer::getAmount)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double sumActs(List<Double> acts) {
        return acts == null ? 0.0 :
                acts.stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .sum();
    }

    public EngineerSummary buildEngineerSummary(String engineerName) {
        List<Engineer> engineers =
                engineerRepository.findAllByEngineerName(engineerName);

        EngineerSummary dto = new EngineerSummary();
        dto.setEngineerName(engineerName);

        List<Double> allActs = engineers.stream()
                .map(Engineer::getActs)          // List<List<Double>>
                .filter(Objects::nonNull)
                .flatMap(List::stream)           // List<Double>
                .filter(Objects::nonNull)
                .toList();

        dto.setActs(allActs);
        dto.setTotalActs(sumActs(allActs));
        dto.setTotalAmount(sumAmounts(engineers));

        return dto;
    }



}
