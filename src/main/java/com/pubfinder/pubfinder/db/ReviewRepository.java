package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

  Optional<Review> findByPubAndReviewer(Pub pub, User user);

  void deleteAllByReviewer(User user);
}
