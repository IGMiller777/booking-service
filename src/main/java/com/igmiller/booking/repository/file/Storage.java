package com.igmiller.booking.repository.file;

import java.util.List;

public interface Storage<T> {
    List<T> loadAll();
    void saveAll(List<T> items);
}
