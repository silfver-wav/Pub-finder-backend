package com.pubfinder.pubfinder.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitedDto {

  private PubDto pubDto;
  private LocalDateTime visitedDate;
}
