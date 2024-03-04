package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PubRepository extends JpaRepository<Pub, UUID> {

    Optional<Pub> findByName(String name);

    @Query(value = "SELECT id, " +
            "(6371 * acos ( cos ( radians(:lat) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:lng) ) + sin ( radians(:lat) ) * sin( radians( lat ) ))) AS distance " +
            "FROM location GROUP BY address GROUP BY HAVING distance < :distance ORDER BY distance ASC", nativeQuery = true)
    List<Pub> filterByLocation(@Param("lat") String lat, @Param("lng")String lng, @Param("distance") int distance);

}
