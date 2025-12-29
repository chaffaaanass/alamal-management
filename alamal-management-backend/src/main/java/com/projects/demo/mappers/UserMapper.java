package com.projects.demo.mappers;


import com.projects.demo.dtos.UserDTO;
import com.projects.demo.entities.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

    public static User toModel(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}
