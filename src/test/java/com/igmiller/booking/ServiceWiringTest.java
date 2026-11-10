package com.igmiller.booking;

import com.igmiller.booking.config.BookingProperties;
import com.igmiller.booking.config.ClockConfig;
import com.igmiller.booking.config.StorageConfig;
import com.igmiller.booking.domain.*;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceWiringTest {
    @TempDir
    Path tempDir;

    @Configuration
    @EnableConfigurationProperties(BookingProperties.class)
    @ComponentScan({"com.igmiller.booking.service", "com.igmiller.booking.repository"})
    @Import({StorageConfig.class, ClockConfig.class})
    static class TestConfig {
    }

    @Test
    void book_shouldWork_whenServicesAreWiredBySpring() {
        new ApplicationContextRunner()
                .withUserConfiguration(TestConfig.class)
                .withPropertyValues(
                        "booking.max-active-per-user=5",
                        "booking.cancel-window=2h",
                        "booking.data-dir=" + tempDir)
                .run(context -> {
                    User ivan = context.getBean(UserRepository.class).save(User.of("Ivan", "ivan@example.com", Role.USER));
                    Resource room = context.getBean(ResourceRepository.class).save(Resource.of("Room", "MR-101", 8,
                            Money.of("100", Currency.USD), ResourceType.MEETING_ROOM, ResourceStatus.ACTIVE));

                    Booking booking = context.getBean(BookingService.class)
                            .book(ivan.getId(), room.getId(), TimeSlot.ofHours(LocalDate.now().plusDays(1), 10, 12));

                    assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
                    assertEquals(Money.of("200", Currency.USD), booking.getPrice());
                });
    }
}