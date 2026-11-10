package com.igmiller.booking.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.NestedExceptionUtils;

import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookingPropertiesTest {
    private final ApplicationContextRunner context = new ApplicationContextRunner().withUserConfiguration(TestConfig.class);

    @EnableConfigurationProperties(BookingProperties.class)
    static class TestConfig {

    }

    private static void assertFailedWith(Throwable failure, String expectedMessage) {
        assertNotNull(failure, "Context should not have started");
        assertEquals(expectedMessage, NestedExceptionUtils.getMostSpecificCause(failure).getMessage());
    }

    @Test
    void bind_shouldAll_values_when_properties_are_valid() {
        context.withPropertyValues(
                "booking.max-active-per-user=5",
                "booking.cancel-window=2h",
                "booking.data-dir=./data"
        ).run((context) -> {
            BookingProperties bookingProperties = context.getBean(BookingProperties.class);

            assertEquals(5, bookingProperties.maxActivePerUser());
            assertEquals(Duration.ofHours(2), bookingProperties.cancelWindow());
            assertEquals(Path.of("data"), bookingProperties.dataDir());
        });
    }

    @Test
    void bind_shouldFail_whenLimitIsNotPositive() {
        context.withPropertyValues(
                "booking.max-active-per-user=-25",
                "booking.cancel-window=2h",
                "booking.data-dir=./data"
        ).run((context) -> {
            assertFailedWith(context.getStartupFailure(),
                    "booking.max-active-per-user must be positive"
            );
        });
    }

    @Test
    void bind_shouldFail_whenPropertiesAreMissing() {
        context.run((context) -> {
            assertFailedWith(
                    context.getStartupFailure(),
                    "booking.max-active-per-user must be positive"
            );
        });
    }

    @Test
    void bind_shouldFail_whenCancelWindowAreMissing() {
        context.withPropertyValues(
                "booking.max-active-per-user=5",
                "booking.data-dir=./data"
        ).run((context) -> {
            assertFailedWith(
                    context.getStartupFailure(),
                    "booking.cancelWindow must not be set and not negative"
            );
        });
    }

    @Test
    void bind_shouldFail_whenDataDirIsMissing() {
        context.withPropertyValues(
                "booking.max-active-per-user=5",
                "booking.cancel-window=2h"
        ).run((context) -> {
            assertFailedWith(
                    context.getStartupFailure(),
                    "booking.data-dir must be set"
            );
        });
    }
}
