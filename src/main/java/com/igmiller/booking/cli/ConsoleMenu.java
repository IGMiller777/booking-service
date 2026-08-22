package com.igmiller.booking.cli;

import com.igmiller.booking.domain.BookingResult;
import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.domain.TimeSlot;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.exception.BookingServiceException;
import com.igmiller.booking.repository.Repository;
import com.igmiller.booking.service.BookingService;
import com.igmiller.booking.service.ResourceService;
import com.igmiller.booking.service.UserService;
import com.igmiller.booking.util.TimeUtils;

public class ConsoleMenu {
    private final InputReader input;
    private final BookingService bookingService;
    private final UserService userService;
    private final ResourceService resourceService;

    private User currentUser;

    public ConsoleMenu(InputReader input, BookingService bookingService, UserService userService, ResourceService resourceService) {
        this.input = input;
        this.bookingService = bookingService;
        this.userService = userService;
        this.resourceService = resourceService;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readInt("Selected: ", 0, 8);
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleListResources();
                case 3 -> handleFreeSlots();
                case 4 -> handleBookResource();
                case 5 -> handleMyBookings();
                case 6 -> handleCancelBooking();
                case 7 -> handleReports();
                case 8 -> handleAdmin();
                case 0 -> running = false;
            }
        }

        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("""
                Welcom to Booking Service
                1. Enter as User
                2. Resources List
                3. Available slot for Resource on Date
                4. Book
                5. My bookings
                6. Cancel Booking
                7. Reports
                8. Admin: Add/Remove resource
                0. Exit
                """);

        if (currentUser != null) {
            System.out.println("Current User: " + currentUser);
        }
    }

    private void handleLogin() {
        long userId = input.readInt("Enter User ID: ", 1, Integer.MAX_VALUE);
        User user = userService.findById(userId);

        if (user == null) {
            System.out.println("Invalid User ID. Try again!");
            return;
        }

        this.currentUser = user;
        System.out.println("Logged in successfully, " + user);
    }

    private void handleListResources() {
        Resource[] resources = resourceService.findAll();

        if (resources.length == 0) {
            System.out.println("Resource List is empty. Try again!");
            return;
        }

        for (Resource resource : resources) {
            System.out.println("Resource ID: " + resource.getId());
        }
    }

    private void handleBookResource() {
        if (!requireLogin()) {
            return;
        }

        long resourceId = input.readInt("Enter Resource ID: ", 1, Integer.MAX_VALUE);

        int startMinute = TimeUtils.parseTime(input.readLine("Start time HH:MM: "));
        int endMinute = TimeUtils.parseTime(input.readLine("End time HH:MM: "));

        if (startMinute == -1 || endMinute == -1) {
            System.out.println("Invalid Time Format. Try again!");
            return;
        }

        TimeSlot slot;
        try {
            slot = TimeSlot.of(startMinute, endMinute);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Time Format." + e.getMessage());
            return;
        }

        try {
            BookingResult result = bookingService.book(currentUser.getId(), resourceId, slot);
            String message = switch (result) {
                case BookingResult.Success s -> "Booked Successfully - " + s.booking().getId();
                case BookingResult.Conflict s -> "Busy - " + s.existing().getSlot();
            };
            System.out.println(message);
        } catch (BookingServiceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error. Check logs");
            e.printStackTrace();
        }
    }

    private void handleFreeSlots() {
        // TODO
        System.out.println("Available Slots for Resource on Date: ");
    }

    private void handleMyBookings() {
        // TODO
        System.out.println("My Bookings");
    }

    private void handleCancelBooking() {
        // TODO
        System.out.println("Cancel Booking on Date: ");
    }

    private void handleReports() {
        // TODO
        System.out.println("Report on Date: ");
    }

    private void handleAdmin() {
        // TODO
        System.out.println("Admin on Date: ");
    }

    private boolean requireLogin() {
        if (currentUser == null) {
            System.out.println("Not logged in. Try again!");
            return false;
        }

        return true;
    }
}
