package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.VisitedRepository;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.Visited;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Visited service.
 */
@Service
public class VisitedService {

  @Autowired
  private VisitedRepository visitedRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PubsService pubsService;

  /**
   * Save visit.
   *
   * @param pubId    the pub id
   * @param username the visitors username
   * @throws ResourceNotFoundException user or pub not found exception
   */
  public void saveVisit(UUID pubId, String username) throws ResourceNotFoundException {
    User user = userService.getUser(username);
    Pub pub = pubsService.getPub(pubId);

    Optional<Visited> uvp = visitedRepository.findByPubAndVisitor(pub, user);
    Visited visited;
    if (uvp.isPresent()) {
      visited = uvp.get();
      visited.setVisitedDate(LocalDateTime.now());
    } else {
      visited = Visited.builder().visitor(user).pub(pub).visitedDate(LocalDateTime.now()).build();
    }
    visitedRepository.save(visited);
  }

  /**
   * Delete visit.
   *
   * @param id the id
   * @throws ResourceNotFoundException the resource not found exception
   */
  public void deleteVisit(UUID id) throws ResourceNotFoundException {
    Visited visited = visitedRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException(
            "Visited with id: " + id + " not found"));
    visitedRepository.delete(visited);
  }

}
