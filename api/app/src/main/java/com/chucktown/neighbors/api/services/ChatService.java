package com.chucktown.neighbors.api.services;

import com.chucktown.neighbors.api.models.ChatRoom;
import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public List<ChatRoom> getAllChatRooms() {
        log.info("Entered getAllChatRooms");

        // get all users from database
        Iterator<ChatRoom> iterator = chatRepository.findAll().iterator();
        List<ChatRoom> newList = new ArrayList<>();
        while(iterator.hasNext()){
            newList.add(iterator.next());
        }
        return newList;
    }

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        log.info("Entered createChatRoom");
        log.info("Parameters: [{}]", chatRoom);

        return chatRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(Long id) {
        log.info("Entered getChatRoom");
        log.info("Parameters: {}", id);

        // get user from database
        Optional<ChatRoom> chatRoom = chatRepository.findById(id);
        if (chatRoom.isPresent()) {
            return chatRoom.get();
        }

        return null;
    }

    public void deleteChatRoom(Long id) {
        log.info("Entered deleteChatRoom");
        log.info("Parameters: [{}]", id);

        Optional<ChatRoom> chatRoom = chatRepository.findById(id);
        if (chatRoom.isPresent()) {
            chatRepository.delete(chatRoom.get());
        }
    }

    public ChatRoom addToChatRoom(Long id, Client currentMember, Client newMember) {
        log.info("Entered addToChatRoom");
        log.info("Parameters: [{}] [{}] [{}]", id, currentMember, newMember);

        // Does the chat exits?
        Optional<ChatRoom> chatCheck = chatRepository.findById(id);

        if (!chatCheck.isPresent()) {
            return null;
        }
        log.info("Chat room found");
        ChatRoom chatRoom = chatCheck.get();

        // Does the current member exist in the chat?
        Optional<Client> memberCheck = chatRoom.getClientList().stream().filter(client -> client.getId()==currentMember.getId()).findFirst();

        if (!memberCheck.isPresent()) {
            return null;
        }
        log.info("Existing member in the group");

        Optional<Client> newMemberCheck = chatRoom.getClientList().stream().filter(client -> client.getId()==newMember.getId()).findFirst();

        if (!newMemberCheck.isPresent()) {
            log.info("Adding new member to group.");
            chatRoom.addClient(newMember);
            return chatRepository.save(chatRoom);
        }

        log.info("New member already exists in the room.");
        return chatRoom;
    }
}
