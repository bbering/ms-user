package com.ms.ms_user.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_user.repositories.UserRepository;
import com.ms.ms_user.security.JWTUtil;

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

}
