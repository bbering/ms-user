package com.ms.ms_user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_user.dtos.UserRequestDTO;
import com.ms.ms_user.dtos.UserResponseDTO;
import com.ms.ms_user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ms/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Cria um usuário com os dados fornecidos e retorna os dados criados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userData) {
        UserResponseDTO userDataToReturn = userService.saveNewUser(userData);
        return new ResponseEntity<>(userDataToReturn, HttpStatus.CREATED);
    }
}