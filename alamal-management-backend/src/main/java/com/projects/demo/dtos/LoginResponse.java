package com.projects.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private Instant expiration;
}
