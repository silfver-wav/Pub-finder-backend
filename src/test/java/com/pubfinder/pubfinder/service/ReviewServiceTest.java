package com.pubfinder.pubfinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pubfinder.pubfinder.db.ReviewRepository;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.enums.Volume;
import com.pubfinder.pubfinder.util.TestUtil;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.cache.type=none",
    "bucket4j.enabled=false",
    "spring.datasource.url=",
    "spring.jpa.database-platform=",
    "spring.jpa.hibernate.ddl-auto=none"
})
public class ReviewServiceTest {

  @Autowired
  public ReviewService reviewService;
  @MockBean
  public ReviewRepository reviewRepository;
  @MockBean
  public UserService userService;
  @MockBean
  public PubService pubService;

  @Test
  public void saveReviewTest()
      throws ResourceNotFoundException, ReviewAlreadyExistsException, BadRequestException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenReturn(user);
    when(pubService.getPub(any())).thenReturn(pub);
    when(reviewRepository.findByPubAndReviewer(pub, user)).thenReturn(Optional.empty());
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.save(any())).thenReturn(review);
    when(pubService.updateRatingsInPub(any())).thenReturn(TestUtil.generateMockPubDTO());
    ReviewDto result = reviewService.saveReview(review, pub.getId(), user.getUsername());

    assertEquals(result, Mapper.INSTANCE.entityToDto(review));
    verify(reviewRepository, times(1)).save(review);
  }

  @Test
  public void saveReviewTest_UserNotFound() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenThrow(ResourceNotFoundException.class);
    Review review = TestUtil.generateMockReview(user, pub);
    assertThrows(ResourceNotFoundException.class,
        () -> reviewService.saveReview(review, pub.getId(), user.getUsername()));
  }

  @Test
  public void saveReviewTest_PubNotFound() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenReturn(user);
    when(pubService.getPub(any())).thenThrow(ResourceNotFoundException.class);
    Review review = TestUtil.generateMockReview(user, pub);
    assertThrows(ResourceNotFoundException.class,
        () -> reviewService.saveReview(review, pub.getId(), user.getUsername()));
  }

  @Test
  public void saveReviewTest_ReviewAlreadyExists() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenReturn(user);
    when(pubService.getPub(any())).thenReturn(pub);
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.findByPubAndReviewer(pub, user)).thenReturn(Optional.of(review));
    assertThrows(ReviewAlreadyExistsException.class,
        () -> reviewService.saveReview(review, pub.getId(), user.getUsername()));
  }


  @Test
  public void deleteReviewTest() throws ResourceNotFoundException, BadRequestException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.findById(any())).thenReturn(Optional.of(review));
    doNothing().when(reviewRepository).delete(review);
    when(pubService.updateRatingsInPub(any())).thenReturn(TestUtil.generateMockPubDTO());

    reviewService.deleteReview(review.getId());
    verify(reviewRepository, times(1)).delete(review);
  }

  @Test
  public void deleteReviewTest_ReviewNotFound() {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(review.getId()));
  }

  @Test
  public void updateReviewTest() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.findById(any())).thenReturn(Optional.of(review));
    review.setVolume(Volume.LOUD);
    when(reviewRepository.save(any())).thenReturn(review);

    ReviewDto result = reviewService.updateReview(review);
    assertEquals(Mapper.INSTANCE.entityToDto(review), result);
    verify(reviewRepository, times(1)).save(review);
  }

  @Test
  public void updateReviewTest_ReviewNotFound() {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    Review review = TestUtil.generateMockReview(user, pub);
    when(reviewRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview(review));
  }
}
