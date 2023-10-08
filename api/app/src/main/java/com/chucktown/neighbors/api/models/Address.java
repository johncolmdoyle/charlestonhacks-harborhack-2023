package com.chucktown.neighbors.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private Double latitude;
    private Double longitude;

    @CreationTimestamp
    private LocalDateTime insertedOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    public String getFormattedAddress() {
        return Objects.toString(line1, "") + ", "
                + Objects.toString(city, "") + ", "
                + Objects.toString(state, "") + " "
                + Objects.toString(zipCode, "");
    }
}
