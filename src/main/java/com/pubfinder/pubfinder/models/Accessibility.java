package com.pubfinder.pubfinder.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Accessibility implements Serializable {
    private Boolean accessibleSeating;
    private Boolean accessibleEntrance;
    private Boolean accessibleParking;
}
