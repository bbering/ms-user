package com.ms.ms_user.service;

import org.springframework.beans.BeanUtils;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import com.ms.ms_user.models.User;
import com.ms.ms_user.repositories.UserRepository;

public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User toEntity(UserRequestDTO userRequest) {
        User userMapped = new User();
        BeanUtils.copyProperties(userRequest, userMapped);
        return userMapped;
    }

    public UserResponseDTO toDTO(User userData) {
        UserResponseDTO userDataToReturn = new UserResponseDTO();
        BeanUtils.copyProperties(userData, userDataToReturn);
        return userDataToReturn;
    }
}
