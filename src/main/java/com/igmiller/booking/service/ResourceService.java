package com.igmiller.booking.service;

import com.igmiller.booking.domain.Resource;
import com.igmiller.booking.repository.ResourceRepository;

import java.util.List;

public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }
}
