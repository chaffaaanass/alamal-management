package com.projects.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class EngineerType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long engineerTypeId;

    @Column(unique = true)
    private String engineerType;

    @OneToMany(mappedBy = "engineerType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Engineer> engineers = new ArrayList<>();

}
