package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.Role;
import com.igmiller.booking.domain.User;
import com.igmiller.booking.exception.StorageException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvUserStorage implements Storage<User> {
    private final Path filePath;

    public CsvUserStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<User> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<User> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    result.add(fromCsvLine(line));
                }
            }
        } catch (IOException e) {
            throw new StorageException("Не удалось прочитать " + filePath, e);
        }

        return result;
    }

    @Override
    public void saveAll(List<User> items) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write("id;name;email;role");
            writer.newLine();
            for (User user : items) {
                writer.write(toCsvLine(user));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Не удалось записать " + filePath, e);
        }
    }

    private User fromCsvLine(String line) {
        String[] parts = line.split(";");

        long id = Long.parseLong(parts[0]);
        String name = parts[1];
        String email = parts[2];
        Role role = Role.valueOf(parts[3]);

        return User.restore(id, name, email, role);
    }

    private String toCsvLine(User user) {
        return user.getId() + ";" +
                user.getName() + ";" +
                user.getEmail() + ";" +
                user.getRole();
    }

}
