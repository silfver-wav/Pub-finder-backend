package com.pubfinder.pubfinder.models;

import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import com.pubfinder.pubfinder.models.enums.Rating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

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