package com.ms.ms_user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import org.springframework.security.authentication.*;
import com.ms.ms_user.repositories.UserRepository;
import com.ms.ms_user.security.JWTUtil;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final AuthenticationManager authenticationManager;

    final UserRepository userRepository;

    final PasswordEncoder passEncoder;

    final JWTUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
            PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDTO> authUser(@Valid @RequestBody UserRequestDTO user){
        //
    }

}
