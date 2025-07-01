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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "Endpoints for user sign-in and registration")
public class AuthController {

    @Autowired
    @Lazy
    private UserService userService;

    private final JWTUtil jwtUtil;

    public AuthController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Sign in with email and password", description = "Authenticates the user and returns a JWT token if credentials are valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json"))
    })
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

    @Operation(summary = "Register a new user", description = "Creates a new user with the given email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Email already registered", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO user) {
        try {
            UserResponseDTO userResponse = userService.saveNewUser(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao registrar usuário.");
        }
    }

    @Schema(description = "DTO representing the JWT token response")
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
