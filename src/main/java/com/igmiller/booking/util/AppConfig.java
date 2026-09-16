package com.igmiller.booking.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class AppConfig {
    private final Properties props;

    private AppConfig(Properties props) {
        this.props = props;
    }

    public static AppConfig load(Path path) {
        Properties props = new Properties();

        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                props.load(reader);
            } catch (IOException e) {
                System.out.println("Не удалось прочитать " + path + ", используются значения по умолчанию");
            }
        }

        return new AppConfig(props);
    }

    public String getString(String key, String defaultValue) {
        String fromEnv = System.getenv(toEnvKey(key));
        if (fromEnv != null) {
            return fromEnv;
        }

        String fromSystem = System.getProperty(key);
        if (fromSystem != null) {
            return fromSystem;
        }

        return props.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String raw = getString(key, null);
        if (raw == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public LocalTime getTime(String key, LocalTime defaultValue) {
        String raw = getString(key, null);
        if (raw == null) {
            return defaultValue;
        }
        try {
            return LocalTime.parse(raw.trim());
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    public Path getPath(String key, Path defaultValue) {
        String raw = getString(key, null);
        return raw == null ? defaultValue : Path.of(raw.trim());
    }

    private static String toEnvKey(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}
