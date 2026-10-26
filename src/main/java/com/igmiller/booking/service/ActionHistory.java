package com.igmiller.booking.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ActionHistory {
    private static final int MAX_SIZE = 10;
    private final Deque<String> actions = new ArrayDeque<>();

    public void record(String action) {
        if (actions.size() >= MAX_SIZE) {
            actions.removeLast();
        }

        actions.addFirst(action);
    }

    public List<String> recent() {
        return List.copyOf(actions);
    }
}
