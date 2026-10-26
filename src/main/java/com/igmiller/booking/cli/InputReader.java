package com.igmiller.booking.cli;

import java.util.Locale;
import java.util.Scanner;

public class InputReader {
    private final Scanner scanner = new Scanner(System.in);

    public int readInt(String prompt, int min, int max) {
        boolean isValidInput = false;
        int result = 0;

        while (!isValidInput) {
            System.out.println(prompt);
            String line = scanner.nextLine().trim();

            try {
                int input = Integer.parseInt(line);
                if (input >= min && input <= max) {
                    isValidInput = true;
                    result = input;
                } else {
                    System.out.println("The number between" + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("This is not a number. Try again.");
            }

        }

        return result;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);

        return scanner.nextLine().trim();
    }

    public boolean readYesOrNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

            switch (answer) {
                case "yes", "y", "д", "да":
                    return true;
                case "no", "n", "н", "нет":
                    return false;
                default:
                    System.out.println("Invalid input. Enter Yes or No.");
            }
        }
    }
}
