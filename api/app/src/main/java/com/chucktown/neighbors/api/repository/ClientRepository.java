package com.chucktown.neighbors.api.repository;

import com.chucktown.neighbors.api.models.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Client findById(long id);
    Optional<Client> findByAuth0Id(String auth0Id);

    @Query(value = "SELECT c.* FROM Client c JOIN Address a ON c.address_id=a.id WHERE (point(:longitude,:latitude) <@> point(a.longitude,a.latitude)) < c.max_distance",
            nativeQuery = true)
    List<Client> findClientsWithinDistance(@Param("latitude") double latitude,
                                           @Param("longitude") double longitude);
}
