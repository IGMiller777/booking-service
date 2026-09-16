package com.igmiller;

import com.igmiller.booking.cli.ConsoleMenu;
import com.igmiller.booking.cli.InputReader;
import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.BookingRepository;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.repository.file.*;
import com.igmiller.booking.service.*;
import com.igmiller.booking.util.AppConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        AppConfig config = AppConfig.load(Path.of("config.properties"));
        Path dataDir = config.getPath("data.dir", Path.of("data"));

        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create dir for date", e);
        }

        InputReader input = new InputReader();
        Storage<Booking> bookingStorage = new CsvBookingStorage(dataDir.resolve("bookings.csv"));
        BookingRepository bookingRepository = new FileBookingRepository(bookingStorage);

        Storage<Resource> resourceStorage = new CsvResourceStorage(dataDir.resolve("resources.csv"));
        ResourceRepository resourcesRepository = new FileResourceRepository(resourceStorage);

        Storage<User> userStorage = new CsvUserStorage(dataDir.resolve("users.csv"));
        UserRepository usersRepository = new FileUserRepository(userStorage);

        PricingService pricingService = new PricingService();
        BookingService bookingService = new BookingService(bookingRepository, resourcesRepository, pricingService, config);
        UserService userService = new UserService(usersRepository);
        ResourceService repositoryService = new ResourceService(resourcesRepository);

        ActionHistory actionHistory = new ActionHistory();
        NotificationSender notificationSender = new EmailSender();
        NotificationService notificationService = new NotificationService(notificationSender);

        ConsoleMenu consoleMenu = new ConsoleMenu(input, bookingService, userService, repositoryService, actionHistory, notificationService);

        consoleMenu.run();
        notificationService.close();
    }
}