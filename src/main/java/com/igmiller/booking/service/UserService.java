package com.igmiller.booking.service;

import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.UserRepository;

public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }
}
