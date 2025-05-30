package com.ms.ms_user.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.ms_user.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByName(String name);
}
