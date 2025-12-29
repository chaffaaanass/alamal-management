package com.projects.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Engineer> engineers = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Society> societies = new ArrayList<>();
}
