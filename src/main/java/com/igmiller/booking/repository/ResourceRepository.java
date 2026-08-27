package com.igmiller.booking.repository;

import com.igmiller.booking.domain.Resource;

public interface ResourceRepository extends Repository<Resource, Long> {
    Resource findByCode(String code);
}
