package com.pubfinder.pubfinder.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Entity
@Table(name = "pub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pub {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double lat;
    @Column(nullable = false)
    private Double lng;
    @Column()
    private String open;
    @Column(nullable = false)
    private String location;
    @Column()
    private String description;

    public Pub(String name, Double lat, Double lng, String open, String location, String description) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.open = open;
        this.location = location;
        this.description = description;
    }
}
