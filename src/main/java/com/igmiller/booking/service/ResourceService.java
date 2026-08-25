package com.igmiller.booking.service;

import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.repository.Repository;

import java.util.List;

public class ResourceService {
    private final Repository<Resource, Long> resourceRepository;

    public ResourceService(Repository<Resource, Long> resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }
}
