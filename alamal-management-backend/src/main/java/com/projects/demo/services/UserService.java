package com.projects.demo.services;

import com.projects.demo.dtos.UserDTO;
import com.projects.demo.entities.User;
import com.projects.demo.exceptions.UserNotFoundException;
import com.projects.demo.mappers.UserMapper;
import com.projects.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO).toList();
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("L'utilisateur est introuvable."));

        return UserMapper.toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User createdUser = new User();
        createdUser.setUsername(userDTO.getUsername());
        createdUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(createdUser);

        return UserMapper.toDTO(createdUser);
    }

    public UserDTO updateUser(String username, UserDTO userDTO) {
        User updatedUser = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("L'utilisateur est introuvable."));
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(updatedUser);

        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}
