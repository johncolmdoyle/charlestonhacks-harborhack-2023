package com.example.helloworld.controllers;

import com.example.helloworld.models.Message;
import com.example.helloworld.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
