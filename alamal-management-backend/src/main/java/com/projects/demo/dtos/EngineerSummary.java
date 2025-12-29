package com.projects.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EngineerSummary {
    private List<Double> acts;
    private String engineerName;
    private Double totalActs;
    private Double totalAmount;
}
