package com.ms.ms_user.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import com.ms.ms_user.service.UserService;
import com.ms.ms_user.security.JWTUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    @Lazy
    private UserService userService;

    private final JWTUtil jwtUtil;

    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@Valid @RequestBody UserRequestDTO user) {
        try {
            String username = userService.authenticate(user.getEmail(), user.getPassword());

            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok().body(new TokenResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO user) {
        try {
            UserResponseDTO userResponse = userService.saveNewUser(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado.");
        }

    }

    public static class TokenResponseDTO {
        private final String token;

        public TokenResponseDTO(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
