package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.UserVisitedPub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserVisitedPubRepository extends JpaRepository<UserVisitedPub, UUID> {
    Optional<UserVisitedPub> findByPubAndUser(Pub pub, User user);

    List<UserVisitedPub> findAllByUser(User user);

    void deleteAllByUser(User user);
}
