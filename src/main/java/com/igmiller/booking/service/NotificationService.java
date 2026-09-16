package com.igmiller.booking.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationService implements AutoCloseable {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final NotificationSender sender;

    public NotificationService(NotificationSender notificationSender) {
        this.sender = notificationSender;
    }

    public CompletableFuture<Void> notifyAsync(String recipient, String message) {
        return CompletableFuture
                .runAsync(() -> sender.send(recipient, message))
                .orTimeout(5, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    System.out.println("Sending message to " + recipient + ": " + ex.getMessage());
                    return null;
                });
    }

    @Override
    public void close() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
