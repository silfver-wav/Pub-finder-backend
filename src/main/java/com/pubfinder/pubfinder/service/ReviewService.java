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

    public List<ReviewDTO> getReviewsForPub(UUID pubId) throws ResourceNotFoundException {
        Pub pub = pubsService.getPub(pubId);
        return reviewRepository.findAllByPub(pub)
                .stream()
                .map(Mapper.INSTANCE::entityToDto)
                .toList();
    }

    public List<ReviewDTO> getReviewsFormUser(String username) throws ResourceNotFoundException {
        User user = userService.getUser(username);
        return reviewRepository.findAllByUser(user)
                .stream()
                .map(Mapper.INSTANCE::entityToDto)
                .toList();
    }

    public ReviewDTO saveReview(Review review, UUID pubId, String username) throws ResourceNotFoundException, ReviewAlreadyExistsException {
        User user = userService.getUser(username);
        Pub pub = pubsService.getPub(pubId);
        reviewRepository.findByPubAndUser(pub, user)
                .orElseThrow(() -> new ReviewAlreadyExistsException("User: " + username + " has already made an review on Pub: " + pubId));

        review.setReviewDate(LocalDateTime.now());
        return Mapper.INSTANCE.entityToDto(reviewRepository.save(review));
    }

    public void deleteReview(Review review) {
    }

    public ReviewDTO updateReview(Review review) {
        return new ReviewDTO();
    }
}
