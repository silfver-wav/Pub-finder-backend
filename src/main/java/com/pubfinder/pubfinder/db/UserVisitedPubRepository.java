package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.UserVisitedPub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVisitedPubRepository extends JpaRepository<UserVisitedPub, UUID> {
    Optional<UserVisitedPub> findByPubAndUser(Pub pub, User user);
}
