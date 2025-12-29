package com.projects.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class BatchType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long batchTypeId;

    private String type;
    private Long section;

    @OneToMany(mappedBy = "batchType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Society> societies = new ArrayList<>();
}
