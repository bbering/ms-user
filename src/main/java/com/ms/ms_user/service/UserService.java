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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    // injecao com @Lazy para evitar ciclo de dependencia
    public UserService(UserRepository userRepository,
            UserProducer userProducer,
            @Lazy AuthenticationManager authManager,
            BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authManager;
        this.userProducer = userProducer;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public String authenticate(String username, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    @Transactional
    public UserResponseDTO saveNewUser(UserRequestDTO userData) {
        User userToSave = toEntity(userData);

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        userRepository.save(userToSave);

        userProducer.publishEmailMessage(userData);

        return toDTO(userToSave);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userToFind = userRepository.findUserByEmail(username);

        if (userToFind == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userToFind.getName(),
                userToFind.getPassword(),
                Collections.emptyList());
    }
}
