package com.pubfinder.pubfinder.dto;

import com.pubfinder.pubfinder.models.Accessibility;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.enums.Volume;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PubDto implements Serializable {

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
  private int rating;
  private int toiletRating;
  private int serviceRating;
  private Volume volume;

}