package com.pubfinder.pubfinder.models.Pub;

import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.Visited;
import com.pubfinder.pubfinder.models.enums.Volume;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * The type Pub.
 */
@Entity(name = "Pub")
@Table(name = "pub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pub implements Serializable {

  @Id
  @GeneratedValue
  @Column(unique = true, nullable = false)
  private UUID id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Double lat;
  @Column(nullable = false)
  private Double lng;
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Map<DayOfWeek, List<OpeningHours>> openingHours;
  @Column(nullable = false)
  private String location;
  @Column
  private String description;
  @Column
  private String price;

  @Column
  private int avgRating = 0;

  @Column
  private int avgToiletRating = 0;

  @Column
  private int avgServiceRating = 0;

  @Column
  private Volume avgVolume;

  @OneToMany(mappedBy = "pub", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<Visited> visitors;

  @OneToMany(mappedBy = "pub", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<Review> reviews;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "info_id")
  private AdditionalInfo additionalInfo;

}