package com.weather;

import java.io.IOException;

public class TemperatureConverter {
    private static final double ABSOLUTE_ZERO_CELSIUS = -273.15;


    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }

        String from = args[0].toUpperCase();
        String to = args[1].toUpperCase();

        if (isValidScale(from)) {
            System.err.println("Ошибка: неизвестная шкала «" + from + "». Допустимые: C, F, K.");
            System.exit(1);
        }

        if (isValidScale(to)) {
            System.err.println("Ошибка: неизвестная шкала «" + to + "». Допустимые: C, F, K.");
            System.exit(1);
        }

        double value;

        try {
            value = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: «" + args[2] + "» — не число. Пример: 100, -40, 36.6");
            System.exit(1);

            return;
        }

        double celsius = toCelsius(value, from);

        if(celsius < ABSOLUTE_ZERO_CELSIUS) {
            System.err.printf(
                    "Ошибка: %.2f°%s = %.2f°C — ниже абсолютного нуля (%.2f°C).%n",
                    value, from, celsius, ABSOLUTE_ZERO_CELSIUS
            );
            System.exit(1);
        }

        double result = fromCelsius(celsius, to);

        System.out.println("from=" + from + " to=" + to + " value=" + result);
    }

    private static boolean isValidScale(String scale) {
        return !scale.equals("C") && !scale.equals("F") && !scale.equals("K");
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

    private static void printUsage() {
        System.err.println("Использование: java com.weather.TemperatureConverter <из> <в> <значение>");
        System.err.println("  Шкалы: C (Цельсий), F (Фаренгейт), K (Кельвин)");
        System.err.println("  Примеры:");
        System.err.println("    java com.weather.TemperatureConverter C F 100");
        System.err.println("    java com.weather.TemperatureConverter K C 0");
        System.err.println("    java com.weather.TemperatureConverter F K -459.67");
    }
}
