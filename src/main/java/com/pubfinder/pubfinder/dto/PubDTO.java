package com.pubfinder.pubfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PubDTO {
    private UUID id;
    private String name;
    private Double lat;
    private Double lng;
    private String open;
    private String location;
    private String description;
}