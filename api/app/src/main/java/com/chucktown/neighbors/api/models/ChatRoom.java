package com.chucktown.neighbors.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Boolean interview;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Client> clientList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private List<ChatMessage> messages = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime insertedOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public void addClient(Client client) {
        clientList.add(client);
    }
}
