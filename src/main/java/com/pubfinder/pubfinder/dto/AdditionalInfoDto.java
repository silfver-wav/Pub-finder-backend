package com.pubfinder.pubfinder.dto;

import com.pubfinder.pubfinder.models.Pub.Accessibility;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdditionalInfoDto implements Serializable {

  private String website;
  private Accessibility accessibility;
  private Boolean washroom;
  private Boolean outDoorSeating;
}
