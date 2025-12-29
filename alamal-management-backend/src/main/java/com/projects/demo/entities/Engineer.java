package com.projects.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Engineer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long engineerId;

    @Column(unique = true)
    private Long chequeNumber;
    private String engineerName;
    private Double amount;
    private LocalDate chequeDate;

    @ElementCollection
    @CollectionTable(name = "engineer_act_values", joinColumns = @JoinColumn(name = "engineer_id"))
    @Column(name = "acts")
    private List<Double> acts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineerType_id")
    private EngineerType engineerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User createdBy;
}