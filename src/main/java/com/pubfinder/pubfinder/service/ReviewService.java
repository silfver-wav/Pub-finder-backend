package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.ReviewRepository;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Review service.
 */
@Service
public class ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PubsService pubsService;

  /**
   * Save review review dto.
   *
   * @param review   the review
   * @param pubId    the pub id
   * @param username the user username
   * @return the review dto
   * @throws ResourceNotFoundException    the user or pub not found exception
   * @throws ReviewAlreadyExistsException the review already exists exception
   */
  public ReviewDto saveReview(Review review, UUID pubId, String username)
      throws ResourceNotFoundException, ReviewAlreadyExistsException {
    User user = userService.getUser(username);
    Pub pub = pubsService.getPub(pubId);
    if (reviewRepository.findByPubAndReviewer(pub, user).isPresent()) {
      throw new ReviewAlreadyExistsException(
          "User: " + username + " has already made a review on Pub: " + pubId);
    }

    review.setReviewer(user);
    review.setPub(pub);
    review.setReviewDate(LocalDateTime.now());
    return Mapper.INSTANCE.entityToDto(reviewRepository.save(review));
  }

  /**
   * Delete review.
   *
   * @param id the id
   * @throws ResourceNotFoundException the review not found exception
   */
  public void deleteReview(UUID id) throws ResourceNotFoundException {
    Review review = reviewRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Review: " + id + " not found."));
    reviewRepository.delete(review);
  }

  /**
   * Update review review dto.
   *
   * @param review the review
   * @return the review dto
   * @throws ResourceNotFoundException the review not found exception
   */
  public ReviewDto updateReview(Review review) throws ResourceNotFoundException {
    reviewRepository.findById(review.getId()).orElseThrow(
        () -> new ResourceNotFoundException("Review: " + review.getId() + " not found."));
    review.setReviewDate(LocalDateTime.now());
    return Mapper.INSTANCE.entityToDto(reviewRepository.save(review));
  }

}
