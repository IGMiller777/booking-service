package com.igmiller.booking.service;

public class EmailSender implements NotificationSender {
    private static final System.Logger LOG = System.getLogger(EmailSender.class.getName());

    @Override
    public void send(String recipient, String message) {
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        LOG.log(System.Logger.Level.INFO, "Sending email to {0}: {1}", recipient, message);
    }
}
