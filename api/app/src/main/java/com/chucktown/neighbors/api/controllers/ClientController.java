package com.chucktown.neighbors.api.controllers;

import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public Client createClient(Principal principal, @RequestBody Client client) {
        log.info("Entered createClient");
        log.info("Parameters: [{}]", client);

        client.setAuth0Id(principal.getName());
        try {
            return clientService.createClient(client);
        } catch(Exception e) {
            // unique constraint.
            log.error("Exception: {}", e.getMessage());
            return client;
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public Client getClient(@PathVariable("id") Long id) {
        log.info("Entered getClient");
        log.info("Parameters: [{}]", id);

        return clientService.getClient(id);
    }

    @PutMapping(value = {"/{id}", "/{id}/"})
    public Client updateClient(Principal principal,@PathVariable("id") Long id, @RequestBody Client client) {
        client.setAuth0Id(principal.getName());
        return clientService.updateClient(id, client);
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity deleteClient(@PathVariable("id") Long id) {
       clientService.deleteClient(id);

        return ResponseEntity.ok().build();
    }
}
