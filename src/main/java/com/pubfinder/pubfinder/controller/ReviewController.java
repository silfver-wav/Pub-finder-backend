package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.ReviewService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Review controller.
 */
@RestController
@RequestMapping("/review")
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @PostMapping("/save/{pubId}/{username}")
  public ResponseEntity<ReviewDto> save(@RequestBody ReviewDto review,
      @PathVariable("pubId") UUID pudId, @PathVariable("username") String username)
      throws ReviewAlreadyExistsException, ResourceNotFoundException {
    ReviewDto reviewDto = reviewService.saveReview(Mapper.INSTANCE.dtoToEntity(review), pudId,
        username);
    return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws ResourceNotFoundException {
    reviewService.deleteReview(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/edit")
  public ResponseEntity<ReviewDto> edit(@RequestBody ReviewDto review)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(reviewService.updateReview(Mapper.INSTANCE.dtoToEntity(review)));
  }
}
