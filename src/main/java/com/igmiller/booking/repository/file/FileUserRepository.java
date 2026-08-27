package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.repository.UsersRepository;

import java.util.List;

public class FileUserRepository implements UserRepository {
    private final Storage<User> storage;
    private final UsersRepository cache;

    public FileUserRepository(Storage<User> storage) {
        this.storage = storage;
        this.cache = new UsersRepository();

        for (User user : storage.loadAll()) {
            cache.save(user);
        }
    }

    @Override
    public User save(User entity) {
        User saved = cache.save(entity);
        persist();
        return saved;
    }

    @Override
    public boolean delete(Long id) {
        boolean deleted = cache.delete(id);
        persist();
        return deleted;
    }

    @Override
    public User findById(Long id) {
        return cache.findById(id);
    }

    @Override
    public List<User> findAll() {
        return cache.findAll();
    }

    private void persist() {
        storage.saveAll(cache.findAll());
    }
}
