package com.projects.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SocietyResponseDTO {
    private Long attachmentNumber;
    private Long chequeNumber;
    private String societyName;
    private String date;
    private Double sum;

    private List<Double> batches;

    private String batchType;
    private Long section;
    private String createdBy;
}
