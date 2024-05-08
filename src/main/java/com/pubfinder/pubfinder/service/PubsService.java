package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.ToIntFunction;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * The type Pubs service.
 */
@Service
public class PubsService {

  @Autowired
  private PubRepository pubRepository;

  @Autowired
  private UserService userService;

  /**
   * Gets pub.
   *
   * @param id the pub id
   * @return the pub
   * @throws ResourceNotFoundException the resource not found exception
   */
  @Cacheable(value = "getPub")
  public Pub getPub(UUID id) throws ResourceNotFoundException {
    return pubRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Pub with id " + id + " was not found"));
  }

  /**
   * Search pubs by term list.
   *
   * @param term the term
   * @return list of pubs
   */
  @Cacheable(value = "getPubsByTerm",
      condition = "#term.length() > 1 && #term.length() < 9",
      key = "#term"
  )
  public List<PubDto> searchPubsByTerm(String term) {
    List<Object[]> pubs = pubRepository.findPubsByNameContaining(term);
    List<PubDto> pubsList = new ArrayList<>();
    for (Object[] pub : pubs) {
      pubsList.add(PubDto.builder().id((UUID) pub[0]).name((String) pub[1]).build());
    }
    return pubsList;
  }

  /**
   * Gets pubs within the radius of the location(lat, lng).
   *
   * @param lat    the latitude
   * @param lng    the longitude
   * @param radius the radius
   * @return the pubs
   */
  @Cacheable(value = "getPubs",
      condition = "#radius <= 10",
      key = "#lat.toString().substring(0, 5) + '-' + "
          + "#lng.toString().substring(0, 5) + '-' + "
          + "#radius.toString()")
  public List<PubDto> getPubs(Double lat, Double lng, Double radius) {
    return pubRepository.findPubsWithInRadius(lat, lng, radius).stream()
        .map(Mapper.INSTANCE::entityToDto).toList();
  }

  /**
   * Save pub.
   *
   * @param pub the pub
   * @return the pub dto
   * @throws BadRequestException the pub param is empty
   */
  public PubDto save(Pub pub) throws BadRequestException {
    if (pub == null) {
      throw new BadRequestException();
    }
    Pub savedPub = pubRepository.save(pub);
    return Mapper.INSTANCE.entityToDto(savedPub);
  }

  /**
   * Edit pub.
   *
   * @param pub the pub
   * @return the pub dto
   * @throws ResourceNotFoundException the resource not found exception
   * @throws BadRequestException       the pub param is empty
   */
  public PubDto edit(Pub pub) throws ResourceNotFoundException, BadRequestException {
    if (pub == null || pub.getId() == null) {
      throw new BadRequestException();
    }

    pubRepository.findById(pub.getId()).orElseThrow(
        () -> new ResourceNotFoundException("Pub with id " + pub.getId() + " was not found"));

    Pub savedPub = pubRepository.save(pub);
    return Mapper.INSTANCE.entityToDto(savedPub);
  }

  /**
   * Delete.
   *
   * @param pub the pub
   * @throws BadRequestException the pub param is empty
   */
  public void delete(Pub pub) throws BadRequestException {
    if (pub == null) {
      throw new BadRequestException();
    }
    pubRepository.delete(pub);
  }

  /**
   * Gets reviews.
   *
   * @param id the id
   * @return the reviews
   */
  public List<ReviewDto> getReviews(UUID id) {
    return pubRepository.findAllReviewsForPub(id)
        .stream()
        .map(Mapper.INSTANCE::entityToDto)
        .toList();
  }

  protected PubDto updateRatingsInPub(Pub pub) throws BadRequestException, ResourceNotFoundException {
    List<ReviewDto> reviews = getReviews(pub.getId());

    pub.setAvgRating(calculateAverageRating(reviews, ReviewDto::getRating));
    pub.setAvgToiletRating(calculateAverageRating(reviews, ReviewDto::getToilets));
    pub.setAvgServiceRating(calculateAverageRating(reviews, ReviewDto::getService));
    pub.setAvgVolume(calculateAverageVolume(reviews));

    return edit(pub);
  }

  private int calculateAverageRating(List<ReviewDto> reviews, ToIntFunction<ReviewDto> extractor) {
    int[] ratings = reviews.stream()
        .mapToInt(extractor)
        .filter(rating -> rating != 0)
        .toArray();

    return calculateAverage(Arrays.stream(ratings).sum(), ratings.length);
  }

  private LoudnessRating calculateAverageVolume(List<ReviewDto> reviews) {
    int[] loudness = reviews.stream().filter(r -> r.getLoudness() != null)
        .mapToInt(r -> r.getLoudness().getOrdinal()).toArray();

    int value = calculateAverage(Arrays.stream(loudness).sum(), loudness.length);
    return LoudnessRating.values()[value];
  }

  private int calculateAverage(int sum, int length) {
    return (int) Math.round((double) sum / length);
  }
}
