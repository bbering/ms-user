package com.ms.ms_user.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEventDTO {
    private UUID id;
    private String name;
    private String email;
}