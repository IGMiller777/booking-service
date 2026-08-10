package com.booking.cli;

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
        boolean isValidInput = false;
        boolean result = false;

        while (!isValidInput) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim();

            if (answer.contains("y")) {
                isValidInput = true;
                return true;
            } else if (answer.contains("n")) {
                isValidInput = true;
                return false;
            }
        }

        return result;
    }
}
