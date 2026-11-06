package com.igmiller.booking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.time.Duration;

@ConfigurationProperties(prefix = "booking")
public record BookingProperties(int maxActivePerUser, Duration cancelWindow, Path dataDir) {

    public BookingProperties {
        if (maxActivePerUser <= 0) {
            throw new IllegalArgumentException("booking.max-active-per-user must be positive");
        }

        if (cancelWindow == null || cancelWindow.isNegative()) {
            throw new IllegalArgumentException("booking.cancelWindow must not be set and not negative");
        }

        if (dataDir == null) {
            throw new IllegalArgumentException("booking.data-dir must be set");
        }
    }
}
