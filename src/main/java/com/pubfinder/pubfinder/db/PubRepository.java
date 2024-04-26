package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PubRepository extends JpaRepository<Pub, UUID> {

    Optional<Pub> findByName(String name);

    String HAVERSINE_FORMULA = "(6371 * acos(cos(radians(:lat)) * cos(radians(p.lat)) *" +
            " cos(radians(p.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(p.lat))))";

    @Query("SELECT p FROM Pub p WHERE " + HAVERSINE_FORMULA + " < :distance ORDER BY " + HAVERSINE_FORMULA )
    List<Pub> findPubsWithInRadius(@Param("lat") double lat, @Param("lng") double lng, @Param("distance") double distance);

    @Query("SELECT p.id, p.name FROM Pub p WHERE LOWER(p.name) LIKE %:term% OR LOWER(p.name) LIKE %:term%")
    List<Object[]> findPubsByNameContaining(@Param("term") String term);

    @Query("SELECT u.reviews FROM Pub u WHERE u.id = :id")
    List<Review> findAllReviewsForPub(@Param("id") UUID id);
}
