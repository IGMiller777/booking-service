package com.igmiller;

import com.igmiller.booking.BookingApplication;
import com.igmiller.booking.cli.ConsoleMenu;
import com.igmiller.booking.cli.InputReader;
import com.igmiller.booking.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(BookingApplication.class, args)) {
            ConsoleMenu consoleMenu = new ConsoleMenu(
                    new InputReader(),
                    context.getBean(BookingService.class),
                    context.getBean(UserService.class),
                    context.getBean(ResourceService.class),
                    new ActionHistory(),
                    context.getBean(NotificationService.class)
            );

            consoleMenu.run();
        }
    }
}