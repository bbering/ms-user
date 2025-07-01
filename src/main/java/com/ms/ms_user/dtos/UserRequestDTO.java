package com.ms.ms_user.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRequestDTO {
    private UUID userId;

    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
