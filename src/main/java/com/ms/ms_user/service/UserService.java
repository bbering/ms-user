package com.ms.ms_user.service;

import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
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

    private final AuthenticationManager authenticationManager;

    // injeção com a tag @Lazy para evitar ciclo de dependencias
    public UserService(UserRepository userRepository, UserProducer userProducer, @Lazy AuthenticationManager authManager) {
        this.authenticationManager = authManager;
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

    // metodo auxiliar para ser utilizado no processo de autenticação
    public String authenticate(String username, String rawPassword) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, rawPassword)
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
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
        User userToFind = userRepository.findUserByName(username);

        if (userToFind == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userToFind.getName(),
                userToFind.getEmail(),
                Collections.emptyList());
    }
}
