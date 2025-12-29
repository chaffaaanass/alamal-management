package com.projects.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EngineerRequestDTO {
    @NotNull(message = "Le numéro de chèque est requis.")
    private Long chequeNumber;

    @NotBlank(message = "L'ingénieur est requis.")
    private String engineerName;

    @NotNull(message = "le montant est requis.")
    private Double amount;

    @NotBlank(message = "La date est requis.")
    private String chequeDate;

    private List<Double> acts;

    @NotBlank(message = "le type de l'ingénieur est requis.")
    private String engineerType;

    @NotBlank(message = "Utilisateur est requis.")
    private String createdBy;
}