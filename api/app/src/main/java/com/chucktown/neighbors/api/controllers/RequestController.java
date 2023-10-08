package com.chucktown.neighbors.api.controllers;

import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.services.ClientService;
import com.chucktown.neighbors.api.models.AssisterState;
import com.chucktown.neighbors.api.models.Request;
import com.chucktown.neighbors.api.services.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    private final ClientService clientService;

    @GetMapping
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @PostMapping
    public Request createRequest(Principal principal, @RequestBody Request request) {
        log.info("Entered createRequest");
        log.info("Parameters: [{}]", request);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());
        request.setRequestor(currentClient);

        try {
            return requestService.createRequest(request);
        } catch(Exception e) {
            // unique constraint.
            log.error("Exception: {}", e.getMessage());
            return request;
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public Request getRequest(@PathVariable("id") Long id) {
        log.info("Entered getRequest");
        log.info("Parameters: [{}]", id);

        return requestService.getRequest(id);
    }

    @PutMapping(value = {"/{id}", "/{id}/"})
    public Request updateRequest(Principal principal, @PathVariable("id") Long id, @RequestBody Request request) {
        log.info("Entered updateRequest");
        log.info("Parameters: [{}] [{}]", id, request);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());
        Request existing = requestService.getRequestByClient(id, currentClient);

        if (existing != null) {
            request.setRequestor(currentClient);
            return requestService.updateRequest(id, request);
        }

        return null;
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity deleteRequest(Principal principal, @PathVariable("id") Long id) {
        log.info("Entered deleteRequest");
        log.info("Parameters: [{}]", id);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());
        Request existing = requestService.getRequestByClient(id, currentClient);

        if (existing != null) {
            requestService.deleteRequest(id);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = {"/{id}/assist", "/{id}/assist/"})
    public Request offerAssistance(Principal principal, @PathVariable("id") Long id) {
        log.info("Entered offerAssistance");
        log.info("Parameters: [{}]", id);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());

        return requestService.offerAssistance(id, currentClient);
    }

    @PostMapping(value = {"/{id}/assist/{assisterId}", "/{id}/assist/{assisterId}/{state}"})
    public Request updateAssistance(Principal principal,
                                   @PathVariable("id") Long id,
                                   @PathVariable("assisterId") Long assisterId,
                                   @PathVariable("state") String state
                                   )
                                    {
        log.info("Entered updateAssistance");
        log.info("Parameters: [{}] [{}] [{}]", id, assisterId, state);

        AssisterState assisterState = AssisterState.valueOf(state);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());

        return requestService.updateAssistance(id, assisterId,  currentClient, assisterState);
    }
}
