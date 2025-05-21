package com.ms.ms_user.security;

import org.springframework.context.annotation.Configuration;

import com.ms.ms_user.service.UserService;

@Configuration
public class WebSecurityConfig {

    final UserService userService;

    final AuthEntryPoint authEntryPoint;

    public WebSecurityConfig(UserService userService, AuthEntryPoint authEntryPoint) {
        this.userService = userService;
        this.authEntryPoint = authEntryPoint;
    }

}
