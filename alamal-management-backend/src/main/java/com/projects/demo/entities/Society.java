package com.projects.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Society {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attachmentNumber;
    @Column(unique = true)
    private Long chequeNumber;
    private String societyName;
    private LocalDate date;
    private Double sum;

    @ElementCollection
    @CollectionTable(name = "society_batch_values", joinColumns = @JoinColumn(name = "society_id"))
    @Column(name = "batch")
    private List<Double> batches = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchType_id")
    private BatchType batchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User createdBy;
}
