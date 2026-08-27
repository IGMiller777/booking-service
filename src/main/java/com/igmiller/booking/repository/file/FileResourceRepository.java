package com.igmiller.booking.repository.file;

import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.repository.ResourceRepository;
import com.igmiller.booking.repository.ResourcesRepository;

import java.util.List;

public class FileResourceRepository implements ResourceRepository {
    private final Storage<Resource> storage;
    private final ResourcesRepository cache;

    public FileResourceRepository(Storage<Resource> storage) {
        this.storage = storage;
        this.cache = new ResourcesRepository();

        for (Resource resource : storage.loadAll()) {
            cache.save(resource);
        }
    }

    @Override
    public Resource save(Resource entity) {
        Resource saved = cache.save(entity);
        persist();
        return saved;
    }

    @Override
    public boolean delete(Long id) {
        boolean deleted = cache.delete(id);
        persist();
        return deleted;
    }

    @Override
    public Resource findById(Long id) {
        return cache.findById(id);
    }

    public Resource findByCode(String code) {
        return cache.findByCode(code);
    }

    @Override
    public List<Resource> findAll() {
        return cache.findAll();
    }

    private void persist() {
        storage.saveAll(cache.findAll());
    }
}
