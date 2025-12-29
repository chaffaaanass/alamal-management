package com.projects.demo.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EngineerResponseDTO {
    private Long engineerId;
    private Long chequeNumber;
    private String engineerName;
    private Double amount;
    private String chequeDate;
    private List<Double> acts;
    private String engineerType;
    private String createdBy;
}