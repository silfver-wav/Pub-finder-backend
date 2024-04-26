package com.pubfinder.pubfinder.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * User Visited Pubs.
 */
@Entity
@Table(name = "user_visited_pub")
@Data
@Builder
public class Visited {

  @Id
  @GeneratedValue
  @Column(unique = true, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "pub_id", nullable = false)
  private Pub pub;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User visitor;

  @Column(nullable = false)
  private LocalDateTime visitedDate;
}
