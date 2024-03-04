package com.pubfinder.pubfinder.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PubDTO {
    private UUID id;
    private String name;
    private Double[] geocode;
    private String open;
    private String location;
    private String description;

    public PubDTO(UUID id, String name, Double[] geocode, String open, String location, String description) {
        this.id = id;
        this.name = name;
        this.geocode = geocode;
        this.open = open;
        this.location = location;
        this.description = description;
    }
}
