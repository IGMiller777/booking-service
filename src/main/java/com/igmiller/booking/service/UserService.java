package com.igmiller.booking.service;

import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.Repository;

public class UserService {
    public final Repository<User, Long> userRepository;

    public UserService(Repository<User, Long> userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }
}
