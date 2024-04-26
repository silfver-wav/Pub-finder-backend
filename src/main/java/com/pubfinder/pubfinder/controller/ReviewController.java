package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.ReviewDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @PostMapping("/save/{pubId}/{username}")
    public ResponseEntity<ReviewDTO> save(@RequestBody ReviewDTO review, @PathVariable("pubId") UUID pudId, @PathVariable("username") String username) throws ReviewAlreadyExistsException, ResourceNotFoundException {
        ReviewDTO reviewDTO = reviewService.saveReview(Mapper.INSTANCE.dtoToEntity(review), pudId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<ReviewDTO> edit(@RequestBody ReviewDTO review) throws ResourceNotFoundException {
        return ResponseEntity.ok(reviewService.updateReview(Mapper.INSTANCE.dtoToEntity(review)));
    }
}
