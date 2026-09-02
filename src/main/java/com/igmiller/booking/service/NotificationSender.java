package com.igmiller.booking.service;

public interface NotificationSender {
    void send(String recipient, String message);
}
