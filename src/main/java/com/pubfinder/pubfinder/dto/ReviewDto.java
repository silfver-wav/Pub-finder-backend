package com.pubfinder.pubfinder.dto;

import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

  private UUID id;
  private UUID pubId;
  private String pubname;
  private String username;
  private LocalDateTime reviewDate;
  private String review;
  private int rating;
  private int toilets;
  private LoudnessRating loudness;
  private int service;
}
