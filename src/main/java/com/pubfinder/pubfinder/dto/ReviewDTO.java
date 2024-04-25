package com.pubfinder.pubfinder.dto;

import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import com.pubfinder.pubfinder.models.enums.Rating;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private UUID id;
    private UUID pubId;
    private String username;
    private LocalDateTime reviewDate;
    private String review;
    private Rating rating;
    private Rating toilets;
    private LoudnessRating loudness;
    private Rating service;
}
