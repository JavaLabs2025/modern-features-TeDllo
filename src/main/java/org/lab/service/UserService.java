package org.lab.service;

import org.lab.model.User;
import org.lab.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name) {
        User user = new User(
            UUID.randomUUID(),
            name,
            LocalDateTime.now()
        );
        return userRepository.save(user);
    }
}

