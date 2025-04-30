package com.ms.ms_user.service;

import com.ms.ms_user.repositories.UserRepository;

public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
