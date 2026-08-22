package com.igmiller;

import com.igmiller.booking.cli.ConsoleMenu;
import com.igmiller.booking.cli.InputReader;
import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.InMemoryBookingsRepository;
import com.igmiller.booking.repository.InMemoryResourceRepository;
import com.igmiller.booking.repository.InMemoryUserRepository;
import com.igmiller.booking.repository.Repository;
import com.igmiller.booking.service.BookingService;
import com.igmiller.booking.service.PricingService;
import com.igmiller.booking.service.ResourceService;
import com.igmiller.booking.service.UserService;

public class Main {
    public static void main(String[] args) {
        InputReader input = new InputReader();

        Repository<Booking, Long> bookingRepository = new InMemoryBookingsRepository();
        Repository<Resource, Long> resourceRepository = new InMemoryResourceRepository();
        Repository<User, Long> userRepository = new InMemoryUserRepository();

        PricingService pricingService = new PricingService();
        BookingService bookingService = new BookingService(bookingRepository, resourceRepository, pricingService);
        UserService userService = new UserService(userRepository);
        ResourceService repositoryService = new ResourceService(resourceRepository);

        ConsoleMenu consoleMenu = new ConsoleMenu(input, bookingService, userService, repositoryService);

        consoleMenu.run();
    }
}