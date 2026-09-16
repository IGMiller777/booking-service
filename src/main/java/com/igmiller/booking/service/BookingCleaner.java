package com.igmiller.booking.service;

import java.util.concurrent.atomic.AtomicBoolean;

public class BookingCleaner implements Runnable {
    private AtomicBoolean running = new AtomicBoolean(true);

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) {
            cleanup();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Booking cleaner stopped");
    }

    private void cleanup() {
        System.out.println("Checking overdue bookings...");
        // TODO - ADD LOGIC
    }
}
