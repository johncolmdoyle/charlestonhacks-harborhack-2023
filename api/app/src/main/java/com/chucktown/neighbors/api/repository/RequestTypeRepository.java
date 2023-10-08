package com.chucktown.neighbors.api.repository;

import com.chucktown.neighbors.api.models.RequestType;
import org.springframework.data.repository.CrudRepository;

public interface RequestTypeRepository extends CrudRepository<RequestType, Long> {
    RequestType findById(long id);
}
