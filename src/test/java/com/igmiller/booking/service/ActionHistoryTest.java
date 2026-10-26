package com.igmiller.booking.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActionHistoryTest {
    @Test
    void record_shouldKeepOnlyLastTen() {
        ActionHistory history = new ActionHistory();
        for (int i = 1; i <= 15; i++) {
            history.record("action " + i);
        }

        assertEquals(10, history.recent().size());
        assertEquals("action 15", history.recent().get(0));
    }

    @Test
    void recent_shouldNotAllowModification() {
        ActionHistory history = new ActionHistory();
        history.record("a");

        assertThrows(UnsupportedOperationException.class, () -> history.recent().clear());
    }
}
