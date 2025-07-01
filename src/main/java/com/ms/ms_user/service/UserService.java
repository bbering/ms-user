package com.ms.ms_user.service;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import com.ms.ms_user.models.User;
import com.ms.ms_user.producers.UserProducer;
import com.ms.ms_user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;
    final UserProducer userProducer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            UserProducer userProducer,
            @Lazy AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
        this.authenticationManager = authenticationManager;
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

        if (userData.getPassword() == null || userData.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password não pode ser nulo ou vazio");
        }

        userToSave.setPassword(passwordEncoder.encode(userData.getPassword()));

        userRepository.save(userToSave);
        userProducer.publishEmailMessage(userData);
        return toDTO(userToSave);
    }

}
