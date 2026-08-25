package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Role;
import com.igmiller.booking.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    @Test
    void save_shouldMakeEntityFindableById_whenEntityIsNew() {
        User user = User.of("Ivan", "ivan@admin.com", Role.USER);

        repository.save(user);

        assertEquals(user, repository.findById(user.getId()));
    }

    @Test
    void save_shouldNotCreateDuplicate() {
        User user = User.of("Ivan", "ivan@admin.com", Role.USER);

        repository.save(user);
        repository.save(user);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void shouldReturnNull_whenEntityIsNotFound() {
        User user = repository.findById(999L);
        assertNull(user);
    }

    @Test
    void shouldDeleteUser() {
        User user = User.of("Ivan", "ivan@admin.com", Role.USER);
        repository.save(user);

        boolean result = repository.delete(user.getId());

        assertTrue(result);
        assertNull(repository.findById(user.getId()));
    }
}
