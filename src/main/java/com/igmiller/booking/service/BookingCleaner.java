package com.igmiller.booking.service;

import com.igmiller.booking.exception.BookingServiceException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingCleaner implements Runnable {
    private static final System.Logger LOG = System.getLogger(BookingCleaner.class.getName());

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BookingService bookingService;

    public BookingCleaner(BookingService bookingService) {
        this.bookingService = Objects.requireNonNull(bookingService, "bookingService");
    }

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

        try {
            int completed = bookingService.completeFinishedBookings();
            if (completed > 0) {
                LOG.log(System.Logger.Level.INFO, "Completed finished bookings: " + completed);
            }
        } catch (BookingServiceException e) {
            LOG.log(System.Logger.Level.WARNING, "Cleanup skipped", e);
        }
    }
}
