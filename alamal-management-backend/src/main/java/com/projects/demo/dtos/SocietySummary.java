package com.projects.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SocietySummary {
    private List<Double> batches;
    private String societyName;
    private Double totalBatches;
    private Double totalSum;
}
