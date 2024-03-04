package com.pubfinder.pubfinder.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PubDTO {
    private UUID id;
    private String name;
    private Double lat;
    private Double lng;
    private String open;
    private String location;
    private String description;

    public PubDTO(UUID id, String name, Double lat, Double lng, String open, String location, String description) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.open = open;
        this.location = location;
        this.description = description;
    }
}
