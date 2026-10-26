package com.igmiller.booking.service;

import java.util.Objects;
import java.util.concurrent.*;

public class NotificationService implements AutoCloseable {
    private static final int TIMEOUT_SECONDS = 5;
    private final ExecutorService executor = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r, "notification-worker");
        t.setDaemon(true);
        return t;
    });
    private final NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = Objects.requireNonNull(sender, "sender");
    }

    public CompletableFuture<Void> notifyAsync(String recipient, String message) {
        return CompletableFuture
                .runAsync(() -> sender.send(recipient, message), executor)
                .orTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    Throwable cause = (ex instanceof CompletionException && ex.getCause() != null) ? ex.getCause() : ex;
                    System.out.println("Failed to send notification to " + recipient + cause);
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
