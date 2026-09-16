package com.igmiller.booking.service;

public class EmailSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Sending email to " + recipient + ": " + message);
    }
}
