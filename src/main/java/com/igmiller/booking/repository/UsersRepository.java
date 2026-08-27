package com.igmiller.booking.repository;

import com.igmiller.booking.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository implements UserRepository {
    private List<User> items = new ArrayList<User>();

    @Override
    public User save(User entity) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(entity.getId())) {
                items.set(i, entity);

                return entity;
            }
        }

        items.add(entity);

        return entity;
    }

    @Override
    public User findById(Long id) {
        for (User user : items) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<User>(items);
    }

    @Override
    public boolean delete(Long id) {
        return items.removeIf(user -> user.getId().equals(id));
    }
}
