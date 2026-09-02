package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.StorageException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvBookingStorage implements Storage<Booking> {
    private final Path filePath;

    public CsvBookingStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Booking> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<Booking> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    result.add(fromCsvLine(line));
                }
            }
        } catch (IOException e) {
            throw new StorageException("Cant read " + filePath, e);
        }

        return result;
    }

    @Override
    public void saveAll(List<Booking> items) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write("id;userId;resourceId;date;startMinute;endMinute;priceMinor;currency;status");
            writer.newLine();

            for (Booking booking : items) {
                writer.write(toCsvLine(booking));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Cant write " + filePath, e);
        }
    }

    private Booking fromCsvLine(String line) {
        String[] parts = line.split(";");

        long id = Long.parseLong(parts[0]);
        long userId = Long.parseLong(parts[1]);
        long resourceId = Long.parseLong(parts[2]);
        LocalDateTime start = LocalDateTime.parse(parts[3]);
        LocalDateTime end = LocalDateTime.parse(parts[4]);
        long priceMinor = Long.parseLong(parts[5]);
        Currency currency = Currency.valueOf(parts[6]);
        BookingStatus status = BookingStatus.valueOf(parts[7]);

        return Booking.restore(id, userId, resourceId, TimeSlot.of(start, end), Money.ofMinor(priceMinor, currency), status);
    }

    private String toCsvLine(Booking booking) {
        return booking.getId() + ";" +
                booking.getUserId() + ";" +
                booking.getResourceId() + ";" +
                booking.getSlot().getStart() + ";" +
                booking.getSlot().getEnd() + ";" +
                booking.getPrice().getAmount().movePointRight(2).longValue() + ";" +
                booking.getPrice().getCurrency() + ";" +
                booking.getStatus();
    }
}
