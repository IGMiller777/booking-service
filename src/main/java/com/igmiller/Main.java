package com.igmiller;

import com.igmiller.booking.cli.ConsoleMenu;
import com.igmiller.booking.cli.InputReader;
import com.igmiller.booking.domain.Booking;
import com.igmiller.booking.domain.MyCurrency;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.repository.InMemoryBookingsRepository;
import com.igmiller.booking.repository.InMemoryResourceRepository;
import com.igmiller.booking.repository.InMemoryUserRepository;
import com.igmiller.booking.repository.Repository;
import com.igmiller.booking.service.BookingService;
import com.igmiller.booking.service.PricingService;

public class Main {
    public static void main(String[] args) {
        InputReader input = new InputReader();
        Repository<Booking, Long> bookingRepository = new InMemoryBookingsRepository();
        Repository<Resource, Long> resourceRepository = new InMemoryResourceRepository();
        Repository<User, Long> userRepositor = new InMemoryUserRepository();
        PricingService pricingService = new PricingService();
        BookingService bookingService = new BookingService(bookingRepository, resourceRepository, pricingService);
        ConsoleMenu consoleMenu = new ConsoleMenu(input, bookingService, resourceRepository, userRepositor);

        consoleMenu.run();
    }
}