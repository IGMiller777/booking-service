package com.igmiller.booking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void of_shouldRejectNullName() {
        assertThrows(IllegalArgumentException.class, () -> User.of(null, "a@b.com", Role.USER));
    }

    @Test
    void of_shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class, () -> User.of("   ", "a@b.com", Role.USER));
    }

    @Test
    void of_shouldRejectNullAndBlankEmail() {
        assertThrows(IllegalArgumentException.class, () -> User.of("Ivan", null, Role.USER));
        assertThrows(IllegalArgumentException.class, () -> User.of("Ivan", "  ", Role.USER));
    }

    @Test
    void of_shouldRejectMalformedEmail() {
        assertThrows(IllegalArgumentException.class, () -> User.of("Ivan", "not-an-email", Role.USER));
    }

    @Test
    void of_shouldRejectNullRole() {
        assertThrows(IllegalArgumentException.class, () -> User.of("Ivan", "ivan@example.com", null));
    }

    @Test
    void of_shouldAcceptValidUser() {
        assertDoesNotThrow(() -> User.of("Ivan", "ivan@example.com", Role.USER));
    }
}
