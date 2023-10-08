package com.chucktown.neighbors.api.controllers;

import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.services.ChatService;
import com.chucktown.neighbors.api.models.ChatRoom;
import com.chucktown.neighbors.api.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<ChatRoom> getAllChats() {
        return chatService.getAllChatRooms();
    }

    @PostMapping
    public ChatRoom createChatRoom(Principal principal, @RequestBody ChatRoom chatRoom) {
        log.info("Entered createChatRoom");
        log.info("Parameters: [{}]", chatRoom);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());
        chatRoom.setClientList(new ArrayList<>());
        chatRoom.addClient(currentClient);
        try {
            return chatService.createChatRoom(chatRoom);
        } catch (Exception e) {
            // unique constraint.
            log.error("Exception: {}", e.getMessage());
            return chatRoom;
        }
    }


    @GetMapping(value = {"/{id}", "/{id}/"})
    public ChatRoom getChatRoom(@PathVariable("id") Long id) {
        log.info("Entered getChatRoom");
        log.info("Parameters: [{}]", id);

        return chatService.getChatRoom(id);
    }

    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity deleteChatRoom(@PathVariable("id") Long id) {
        chatService.deleteChatRoom(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = {"/{id}/add/{clientId}", "/{id}/add/{clientId}/"})
    public ChatRoom addToChatRoom(Principal principal, @PathVariable("id") Long id, @PathVariable("clientId") Long clientId) {
        log.info("Entered addToChatRoom");
        log.info("Parameters: [{}] [{}]", id, clientId);

        Client currentClient = clientService.getClientByAuth0Id(principal.getName());
        Client newClient = clientService.getClient(clientId);

        return chatService.addToChatRoom(id, currentClient, newClient);
    }
}
