package com.igmiller.booking.config;

import com.igmiller.booking.domain.Role;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.repository.file.FileBookingRepository;
import com.igmiller.booking.repository.file.FileResourceRepository;
import com.igmiller.booking.repository.file.FileUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageWiringTest {
    @TempDir
    Path tempDir;

    @EnableConfigurationProperties(BookingProperties.class)
    static class TestConfig {
    }

    @Test
    void repositories_shouldWriteCsv_toConfiguredDataDir() {
        Path dataDir = tempDir.resolve("data");

        new ApplicationContextRunner()
                .withUserConfiguration(TestConfig.class, StorageConfig.class,
                        FileUserRepository.class, FileResourceRepository.class, FileBookingRepository.class)
                .withPropertyValues(
                        "booking.max-active-per-user=5",
                        "booking.cancel-window=2h",
                        "booking.data-dir=" + dataDir)
                .run(context -> {
                    UserRepository users = context.getBean(UserRepository.class);

                    users.save(User.of("Ivan", "ivan@example.com", Role.USER));

                    assertTrue(Files.exists(dataDir.resolve("users.csv")));
                });
    }
}