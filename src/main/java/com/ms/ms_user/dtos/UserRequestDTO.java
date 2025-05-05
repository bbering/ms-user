package com.ms.ms_user.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private UUID userId;
    private String email;
    private String name;
}
