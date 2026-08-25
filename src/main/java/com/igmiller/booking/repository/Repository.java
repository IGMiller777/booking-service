package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Identifiable;

import java.util.List;

public interface Repository<T extends Identifiable<ID>, ID> {
    T save(T entity);

    T findById(ID id);

    List<T> findAll();

    boolean delete(ID id);
}
