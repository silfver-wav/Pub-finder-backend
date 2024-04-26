package com.pubfinder.pubfinder.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Opening hours.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningHours implements Serializable {

  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  private LocalTime startTime;
  @JsonSerialize(using = LocalTimeSerializer.class)
  @JsonDeserialize(using = LocalTimeDeserializer.class)
  private LocalTime endTime;
}
