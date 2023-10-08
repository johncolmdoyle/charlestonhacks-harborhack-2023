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
import java.util.stream.Collectors;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client requestor;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "request_type_id", referencedColumnName = "id")
    private RequestType requestType;

    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "assister_id", referencedColumnName = "id")
    private List<Assister> assisterList = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime insertedOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public void addAssistor(Assister assister) {
        assisterList.add(assister);
    }

    public Assister getAssister(Long id) {
        return assisterList.stream()
                .filter(assister -> assister.getId() == id)
                .findFirst().get();
    }

    public List<Assister> getApprovedAssisters() {
        return assisterList.stream()
                .filter(assister -> assister.getAssisterState() == AssisterState.APPROVED)
                .collect(Collectors.toList());
    }

    public List<Assister> getNewAssisters() {
        return assisterList.stream()
                .filter(assister -> assister.getAssisterState() == AssisterState.NEW)
                .collect(Collectors.toList());
    }

    public List<Assister> getPendingAssisters() {
        return assisterList.stream()
                .filter(assister -> assister.getAssisterState() == AssisterState.PENDING)
                .collect(Collectors.toList());
    }

    public void replaceAssister(Assister oldAssister, Assister newAssister) {
        int index = assisterList.indexOf(oldAssister);
        if (index != -1) {
            assisterList.set(index, newAssister);
        }
    }


}
