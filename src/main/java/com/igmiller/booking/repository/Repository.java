package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Identifiable;

public interface Repository<T extends Identifiable<ID>, ID> {
    T save(T entity);
    T findById(ID id);
    T[] findAll();
    boolean delete(ID id);
}
