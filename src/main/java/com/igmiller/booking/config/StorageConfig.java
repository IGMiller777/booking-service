package com.igmiller.booking.config;

import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.exception.StorageException;
import com.igmiller.booking.repository.file.CsvBookingStorage;
import com.igmiller.booking.repository.file.CsvResourceStorage;
import com.igmiller.booking.repository.file.CsvUserStorage;
import com.igmiller.booking.repository.file.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class StorageConfig {

    @Bean
    public Path dataDir(BookingProperties bookingProperties) {
        Path dir = bookingProperties.dataDir();

        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new StorageException("Could not create data directory." + dir, e);
        }

        return dir;
    }

    @Bean
    public Storage<User> createUserStorage(Path dataDir) {
        return new CsvUserStorage(dataDir.resolve("users.csv"));
    }

    @Bean
    public Storage<Resource> createResourceStorage(Path dataDir) {
        return new CsvResourceStorage(dataDir.resolve("resources.csv"));
    }

    @Bean
    public Storage<Booking> createBookingStorage(Path dataDir) {
        return new CsvBookingStorage(dataDir.resolve("bookings.csv"));
    }
}
