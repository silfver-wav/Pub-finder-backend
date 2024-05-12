package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.models.Pub.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.enums.Role;

public class MapperImpl implements Mapper {

  @Override
  public PubDto entityToDto(Pub entity) {
    if (entity == null) {
      return null;
    }
    return PubDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .lat(entity.getLat())
        .lng(entity.getLng())
        .openingHours(entity.getOpeningHours())
        .location(entity.getLocation())
        .description(entity.getDescription())
        .additionalInfoDto(entityToDto(entity.getAdditionalInfo()))
        .rating(entity.getAvgRating())
        .volume(entity.getAvgVolume())
        .serviceRating(entity.getAvgServiceRating())
        .toiletRating(entity.getAvgToiletRating())
        .build();
  }

  @Override
  public UserDto entityToDto(User entity) {
    if (entity == null) {
      return null;
    }
    return UserDto.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .firstname(entity.getFirstname())
        .lastname(entity.getLastname())
        .email(entity.getEmail())
        .password(entity.getPassword())
        .build();
  }

  @Override
  public ReviewDto entityToDto(Review entity) {
    return ReviewDto.builder()
        .id(entity.getId())
        .pubname(entity.getPub().getName())
        .pubId(entity.getPub().getId())
        .username(entity.getReviewer().getUsername())
        .reviewDate(entity.getReviewDate())
        .review(entity.getReview())
        .rating(entity.getRating())
        .toilets(entity.getToilets())
        .loudness(entity.getVolume())
        .service(entity.getService())
        .build();
  }

  @Override
  public AdditionalInfoDto entityToDto(AdditionalInfo additionalInfo) {
    return AdditionalInfoDto.builder()
        .website(additionalInfo.getWebsite())
        .accessibility(additionalInfo.getAccessibility())
        .washroom(additionalInfo.getWashroom())
        .outDoorSeating(additionalInfo.getOutDoorSeating())
        .build();
  }

  @Override
  public Pub dtoToEntity(PubDto dto) {
    if (dto == null) {
      return null;
    }

    Pub.PubBuilder builder = Pub.builder()
        .name(dto.getName())
        .lat(dto.getLat())
        .lng(dto.getLng())
        .openingHours(dto.getOpeningHours())
        .location(dto.getLocation())
        .description(dto.getDescription())
        .additionalInfo(dtoToEntity(dto.getAdditionalInfoDto()));

    if (dto.getId() != null) {
      builder.id(dto.getId());
    }

    return builder.build();
  }

  @Override
  public User dtoToEntity(UserDto dto) {
    if (dto == null) {
      return null;
    }
    return User.builder()
        .id(dto.getId())
        .username(dto.getUsername())
        .firstname(dto.getFirstname())
        .lastname(dto.getLastname())
        .email(dto.getEmail())
        .password(dto.getPassword())
        .role(Role.USER)
        .build();
  }


  @Override
  public Review dtoToEntity(ReviewDto dto) {
    return Review.builder()
        .id(dto.getId())
        .review(dto.getReview())
        .rating(dto.getRating())
        .toilets(dto.getToilets())
        .volume(dto.getLoudness())
        .service(dto.getService())
        .build();
  }

  @Override
  public AdditionalInfo dtoToEntity(AdditionalInfoDto dto) {
    return AdditionalInfo.builder()
        .accessibility(dto.getAccessibility())
        .website(dto.getWebsite())
        .washroom(dto.getWashroom())
        .outDoorSeating(dto.getOutDoorSeating())
        .build();
  }
}
