package com.igmiller.booking.service;

import java.util.ArrayDeque;
import java.util.Deque;

public class ActionHistory {
    private static final int MAX_SIZE = 10;
    private final Deque<String> actions = new ArrayDeque<>();

    public void record(String action) {
        if (actions.size() > MAX_SIZE) {
            actions.removeLast();
        }

        actions.addFirst(action);
    }

    public Deque<String> recent() {
        return actions;
    }
}
