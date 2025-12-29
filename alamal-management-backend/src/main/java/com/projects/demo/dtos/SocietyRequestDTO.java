package com.projects.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SocietyRequestDTO {
    @NotNull(message = "Le numéro de cheque est requis.")
    private Long chequeNumber;

    @NotBlank(message = "La société est requis.")
    private String societyName;

    @NotBlank(message = "La date est requis.")
    private String date;

    @NotNull(message = "La somme est requis.")
    private Double sum;

    private List<Double> batches;

    @NotBlank(message = "Le type de lot est requis.")
    private String batchType;

    private Long section;

    @NotBlank(message = "Utilisateur est requis.")
    private String createdBy;
}
