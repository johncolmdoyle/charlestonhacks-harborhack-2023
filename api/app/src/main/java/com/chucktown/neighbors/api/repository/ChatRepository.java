package com.chucktown.neighbors.api.repository;

import com.chucktown.neighbors.api.models.ChatRoom;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<ChatRoom, Long> {
    ChatRoom findById(long id);
}
