package com.igmiller;

import com.igmiller.booking.cli.ConsoleMenu;
import com.igmiller.booking.cli.InputReader;
import com.igmiller.booking.domain.ActionHistory;
import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.BookingsRepository;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.UserRepository;
import com.igmiller.booking.repository.Repository;
import com.igmiller.booking.service.BookingService;
import com.igmiller.booking.service.PricingService;
import com.igmiller.booking.service.ResourceService;
import com.igmiller.booking.service.UserService;

public class Main {
    public static void main(String[] args) {
        InputReader input = new InputReader();

        BookingsRepository bookingRepository = new BookingsRepository();
        Repository<Resource, Long> resourceRepository = new ResourceRepository();
        Repository<User, Long> userRepository = new UserRepository();

        PricingService pricingService = new PricingService();
        BookingService bookingService = new BookingService(bookingRepository, resourceRepository, pricingService);
        UserService userService = new UserService(userRepository);
        ResourceService repositoryService = new ResourceService(resourceRepository);

        ActionHistory actionHistory = new ActionHistory();

        ConsoleMenu consoleMenu = new ConsoleMenu(input, bookingService, userService, repositoryService, actionHistory);

        consoleMenu.run();
    }
}