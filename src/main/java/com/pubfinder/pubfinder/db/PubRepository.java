package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PubRepository extends JpaRepository<Pub, UUID> {

    Optional<Pub> findByName(String name);

}
