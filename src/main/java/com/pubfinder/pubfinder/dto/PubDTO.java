package com.pubfinder.pubfinder.dto;

import com.pubfinder.pubfinder.models.Accessibility;
import com.pubfinder.pubfinder.models.OpeningHours;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PubDTO implements Serializable {
    private UUID id;
    private String name;
    private Double lat;
    private Double lng;
    private Map<DayOfWeek, List<OpeningHours>> openingHours;
    private String location;
    private String description;
    private String price;
    private String website;
    private Accessibility accessibility;
    private Boolean washroom;
    private Boolean outDoorSeating;

}