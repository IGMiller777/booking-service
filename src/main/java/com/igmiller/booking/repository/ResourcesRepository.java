package com.igmiller.booking.repository;

import com.igmiller.booking.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesRepository implements ResourceRepository {
    private final Map<Long, Resource> resourcesById = new HashMap<>();
    private final Map<String, Resource> resourcesByCode = new HashMap<>();

    @Override
    public Resource save(Resource resource) {
        resourcesById.put(resource.getId(), resource);
        resourcesByCode.put(resource.getCode(), resource);

        return resource;
    }

    @Override
    public Resource findById(Long id) {
        return resourcesById.get(id);
    }

    public Resource findByCode(String code) {
        return resourcesByCode.get(code);
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<Resource>(resourcesById.values());
    }

    @Override
    public boolean delete(Long id) {
        Resource resource = resourcesById.get(id);
        if(resource == null) {
            return false;
        }

        resourcesById.remove(id);
        resourcesByCode.remove(resource.getCode());

        return true;
    }
}
