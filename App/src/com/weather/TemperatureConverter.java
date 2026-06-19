package com.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TemperatureConverter {
    public static void main() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String gradeFrom = ask(reader, "Enter the system to convert from (C,F,K): ");
        String gradeTo   = ask(reader, "Enter the system to convert to (C,F,K): ");

        double temperature = Double.parseDouble(
                ask(reader, "Enter the temperature to convert: ")
        );
        double celsius = toCelsius(temperature, gradeFrom);
        double result = fromCelsius(celsius, gradeTo);

        System.out.println(result);
    }

    private static String ask(BufferedReader reader, String prompt) throws IOException {
        String line;
        do {
            System.out.print(prompt);
            line = reader.readLine();
        } while (line == null || line.trim().isEmpty());

        return line.trim().toLowerCase();
    }

    private static double toCelsius(double value, String from) {
        return switch (from.toUpperCase()) {
            case "C" -> value;
            case "F" -> (value - 32) * 5 / 9;
            case "K" -> value - 273.15;
            default -> throw new IllegalArgumentException("Unknown scale: " + from);
        };
    }

    private static double fromCelsius(double celsius, String to) {
        return switch (to.toUpperCase()) {
            case "C" -> celsius;
            case "F" -> celsius * 9 / 5 + 32;
            case "K" -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unknown scale: " + to);
        };
    }
}
