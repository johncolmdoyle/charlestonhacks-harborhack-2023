package com.chucktown.neighbors.api.controllers;

import com.chucktown.neighbors.api.models.RequestType;
import com.chucktown.neighbors.api.services.RequestTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requestTypes")
public class RequestTypeController {

    private final RequestTypeService requestTypeService;

    @GetMapping
    public List<RequestType> getAllRequestTypes() {
        return requestTypeService.getAllRequestTypes();
    }

    @PostMapping
    public RequestType createRequestType(Principal principal, @RequestBody RequestType requestType) {
        log.info("Entered createRequestType");
        log.info("Parameters: [{}]", requestType);

        try {
            return requestTypeService.createRequestType(requestType);
        } catch(Exception e) {
            // unique constraint.
            log.error("Exception: {}", e.getMessage());
            return requestType;
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public RequestType getRequestType(@PathVariable("id") Long id) {
        log.info("Entered getRequestType");
        log.info("Parameters: [{}]", id);

        return requestTypeService.getRequestType(id);
    }

    @PutMapping(value = {"/{id}", "/{id}/"})
    public RequestType updateRequestType(@PathVariable("id") Long id, @RequestBody RequestType requestType) {
        log.info("Entered updateRequestType");
        log.info("Parameters: [{}] [{}]", id, requestType);
        return requestTypeService.updateRequestType(id, requestType);
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity deleteClient(@PathVariable("id") Long id) {
        log.info("Entered deleteClient");
        log.info("Parameters: [{}]", id);

       requestTypeService.deleteRequestType(id);

        return ResponseEntity.ok().build();
    }
}
