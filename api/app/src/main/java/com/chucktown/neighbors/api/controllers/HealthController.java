package com.chucktown.neighbors.api.controllers;

import com.chucktown.neighbors.api.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final MessageService messageService;

    @GetMapping()
    public ResponseEntity checkUp() {
        return ResponseEntity.ok().build();
    }
}
