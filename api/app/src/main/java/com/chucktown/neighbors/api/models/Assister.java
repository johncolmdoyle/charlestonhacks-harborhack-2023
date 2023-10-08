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

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assister {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private AssisterState assisterState;
    private AssisterState requesterState;

    private AssisterState combinedState;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client assister;

    @CreationTimestamp
    private LocalDateTime insertedOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public AssisterState getCombinedState() {
        if (assisterState.equals(requesterState)) return requesterState;

        // if either reject the overall is rejected.
        if (AssisterState.REJECTED.equals(assisterState)) return requesterState;
        if (AssisterState.REJECTED.equals(requesterState)) return requesterState;

        // Only other option is if one is approved and the other is pending, return pending
        return  AssisterState.PENDING;
    }

    public void setCombinedState(AssisterState ignore) {
        combinedState = getCombinedState();
    }
}
