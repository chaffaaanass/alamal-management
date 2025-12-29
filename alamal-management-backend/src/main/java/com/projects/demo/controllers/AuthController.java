package com.projects.demo.controllers;

import com.projects.demo.dtos.LoginRequest;
import com.projects.demo.dtos.LoginResponse;
import com.projects.demo.dtos.UserDTO;
import com.projects.demo.entities.User;
import com.projects.demo.exceptions.UserNotFoundException;
import com.projects.demo.repositories.UserRepository;
import com.projects.demo.security.JwtTokenProvider;
import com.projects.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        String jwt = tokenProvider.generateToken(authentication.getName());
        Instant expiration = tokenProvider.getTokenExpiration();

        // Fetch full user from DB
        User user = userRepository.findById(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        // Set new Authentication with authorities in the SecurityContext
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);


        LoginResponse response = new LoginResponse(
                jwt,
                user.getUsername(),
                expiration
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.createUser(userDTO);
        newUser.setPassword(null); // secure response
        return ResponseEntity.ok(newUser);
    }
}


