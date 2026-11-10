package com.igmiller.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    @Override
    public void send(String recipient, String message) {
        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        log.info("Sending email to {}: {}", recipient, message);
    }
}
