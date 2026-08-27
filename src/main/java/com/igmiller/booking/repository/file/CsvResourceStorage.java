package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.*;
import com.igmiller.booking.exception.StorageException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvResourceStorage implements Storage<Resource> {
    private final Path filePath;

    public CsvResourceStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Resource> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<Resource> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line = reader.readLine();   // заголовок, пропускаем
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
    public void saveAll(List<Resource> items) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write("id;code;name;capacity;hourlyRateMinor;currency;type;status");
            writer.newLine();
            for (Resource resource : items) {
                writer.write(toCsvLine(resource));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("Не удалось записать " + filePath, e);
        }
    }

    private Resource fromCsvLine(String line) {
        String[] parts = line.split(";");

        long id = Long.parseLong(parts[0]);
        String code = parts[1];
        String name = parts[2];
        int capacity = Integer.parseInt(parts[3]);
        long hourlyRateMinor = Long.parseLong(parts[4]);
        Currency currency = Currency.valueOf(parts[5]);
        ResourceType type = ResourceType.valueOf(parts[6]);
        ResourceStatus status = ResourceStatus.valueOf(parts[7]);

        Money hourlyRate = Money.ofMinor(hourlyRateMinor, currency);

        return Resource.restore(id, name, code, capacity, hourlyRate, type, status);
    }

    private String toCsvLine(Resource resource) {
        return resource.getId() + ";" +
                resource.getCode() + ";" +
                resource.getName() + ";" +
                resource.getCapacity() + ";" +
                resource.getHourlyRate().getAmount().movePointRight(2).longValue() + ";" +
                resource.getHourlyRate().getCurrency() + ";" +
                resource.getResourceType() + ";" +
                resource.getStatus();
    }
}
