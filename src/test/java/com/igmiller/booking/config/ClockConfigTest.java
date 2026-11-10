package com.igmiller.booking.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.time.Clock;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClockConfigTest {
    private final ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(ClockConfig.class);

    @Test
    void clock_shouldBe_registered_with_systemTimeZone() {
        runner.run(context -> {
            Clock clock = context.getBean(Clock.class);

            assertEquals(clock.getZone().toString(), ZoneId.systemDefault().toString());
        });
    }
}
