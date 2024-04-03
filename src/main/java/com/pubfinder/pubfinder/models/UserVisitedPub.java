package com.pubfinder.pubfinder.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_visited_pub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVisitedPub {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pub_id", nullable = false)
    private Pub pub;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime visitedDate;
}
