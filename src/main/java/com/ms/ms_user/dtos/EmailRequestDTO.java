package com.ms.ms_user.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDTO {
    private UUID userId;
    private String emailTo;
    private String subject;
    private String content;
}
