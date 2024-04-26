package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.Visited;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  @Query("SELECT u.visitedPubs FROM User u WHERE u.id = :id")
  List<Visited> getVisitedPubs(@Param("id") UUID id);

  @Query("SELECT u.reviews FROM User u WHERE u.username = :username")
  List<Review> findAllReviewsByUser(@Param("username") String username);
}