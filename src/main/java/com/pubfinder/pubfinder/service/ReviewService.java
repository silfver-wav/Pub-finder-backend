package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.ReviewRepository;
import com.pubfinder.pubfinder.dto.ReviewDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PubsService pubsService;

    public ReviewDTO saveReview(Review review, UUID pubId, String username) throws ResourceNotFoundException, ReviewAlreadyExistsException {
        User user = userService.getUser(username);
        Pub pub = pubsService.getPub(pubId);
        if (reviewRepository.findByPubAndReviewer(pub, user).isPresent()) {
            throw new ReviewAlreadyExistsException("User: " + username + " has already made a review on Pub: " + pubId);
        }

        review.setReviewDate(LocalDateTime.now());
        return Mapper.INSTANCE.entityToDto(reviewRepository.save(review));
    }

    public void deleteReview(UUID id) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review: " + id + " not found."));
        reviewRepository.delete(review);
    }

    public ReviewDTO updateReview(Review review) throws ResourceNotFoundException {
        reviewRepository.findById(review.getId()).orElseThrow(() -> new ResourceNotFoundException("Review: " + review.getId() + " not found."));
        review.setReviewDate(LocalDateTime.now());
        return Mapper.INSTANCE.entityToDto(reviewRepository.save(review));
    }
}
