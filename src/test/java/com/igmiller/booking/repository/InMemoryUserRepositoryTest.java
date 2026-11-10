package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Role;
import com.igmiller.booking.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {
    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void save_shouldMakeUserFindableById_whenUserIsNew() {
        User saved = repository.save(User.of("Ivan", "ivan@admin.com", Role.USER));

        assertEquals(saved, repository.findById(saved.getId()));
    }

    @Test
    void save_shouldNotCreateDuplicate() {
        User saved = repository.save(User.of("Ivan", "ivan@admin.com", Role.USER));

        repository.save(saved);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        User user = repository.findById(999L);
        assertNull(user);
    }

    @Test
    void delete_shouldRemoveUser() {
        User saved = repository.save(User.of("Ivan", "ivan@admin.com", Role.USER));

        boolean result = repository.delete(saved.getId());

        assertTrue(result);
        assertNull(repository.findById(saved.getId()));
    }

    @Test
    void save_shouldAssignSequentialIds_whenUsersAreNew() {
        User first = repository.save(User.of("Ivan", "ivan@admin.com", Role.USER));
        User second = repository.save(User.of("Petr", "petr@admin.com", Role.USER));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }
}
