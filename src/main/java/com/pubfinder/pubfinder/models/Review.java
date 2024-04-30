package com.pubfinder.pubfinder.models;

import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import com.pubfinder.pubfinder.models.enums.Rating;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * The type Review.
 */
@Entity(name = "Review")
@Table(name = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Review {
  @Id
  @GeneratedValue
  @Column(unique = true, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "pub_id", referencedColumnName = "id", insertable = true, updatable = true)
  private Pub pub;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = true, updatable = true)
  private User reviewer;

  @Column(nullable = false)
  private LocalDateTime reviewDate;

  @Column(length = 1000)
  private String review;

  @Column(nullable = false)
  @Min(value = 0, message = "Rating must be greater than or equal to 0")
  @Max(value = 5, message = "Rating must be less than or equal to 100")
  private int rating;

  @Column
  @Min(value = 0, message = "Rating must be greater than or equal to 0")
  @Max(value = 5, message = "Rating must be less than or equal to 100")
  private int toilets;

  @Column
  @Enumerated(EnumType.ORDINAL)
  private LoudnessRating loudness;

  @Column
  @Min(value = 0, message = "Rating must be greater than or equal to 0")
  @Max(value = 5, message = "Rating must be less than or equal to 100")
  private int service;
}