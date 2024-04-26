package com.pubfinder.pubfinder.models;

import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import com.pubfinder.pubfinder.models.enums.Rating;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Review.
 */
@Entity(name = "Review")
@Table(name = "review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

  @Id
  @GeneratedValue
  @Column(unique = true, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "pub_id", referencedColumnName = "id")
  private Pub pub;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User reviewer;

  @Column(nullable = false)
  private LocalDateTime reviewDate;

  @Column(length = 1000)
  private String review;
  @Column(nullable = false)
  private Rating rating;
  @Column
  private Rating toilets;
  @Column
  private LoudnessRating loudness;
  @Column
  private Rating service;
}