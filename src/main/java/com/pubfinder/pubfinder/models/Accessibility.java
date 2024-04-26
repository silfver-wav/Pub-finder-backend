package com.pubfinder.pubfinder.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Accessibility.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Accessibility implements Serializable {

  private Boolean accessibleSeating;
  private Boolean accessibleEntrance;
  private Boolean accessibleParking;
}
