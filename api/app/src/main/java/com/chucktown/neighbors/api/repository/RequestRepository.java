package com.chucktown.neighbors.api.repository;

import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.models.Request;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RequestRepository extends CrudRepository<Request, Long> {
    Request findById(long id);
    Optional<Request> findByIdAndRequestor(long id, Client client);
}
