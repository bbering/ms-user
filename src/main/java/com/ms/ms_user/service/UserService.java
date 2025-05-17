package com.ms.ms_user.service;

import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import com.ms.ms_user.models.User;
import com.ms.ms_user.producers.UserProducer;
import com.ms.ms_user.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;

    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userProducer = userProducer;
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

    @Transactional
    public UserResponseDTO saveNewUser(UserRequestDTO userData) {
        User userToSave = toEntity(userData);
        UserResponseDTO userDataToReturn = toDTO(userToSave);
        userRepository.save(userToSave);

        userProducer.publishEmailMessage(userData);

        return userDataToReturn;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userToFind = userRepository.findUserByUsername(username);

        if (userToFind == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userToFind.getName(),
                userToFind.getEmail(),
                Collections.emptyList());
    }
}
