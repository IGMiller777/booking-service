package com.igmiller.booking.service;

import java.util.concurrent.atomic.AtomicBoolean;

public class BookingCleaner implements Runnable {
    private static final System.Logger LOG = System.getLogger(BookingCleaner.class.getName());

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

        LOG.log(System.Logger.Level.INFO, "Booking cleaner stopped");
    }

    private void cleanup() {
        LOG.log(System.Logger.Level.DEBUG, "Checking overdue bookings...");
        // TODO - ADD LOGIC
    }
}
