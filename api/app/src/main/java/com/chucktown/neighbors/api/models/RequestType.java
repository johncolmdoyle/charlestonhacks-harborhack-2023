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
public class RequestType {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String typeName;
    private String typeImageUrl;

    @CreationTimestamp
    private LocalDateTime insertedOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;
}
