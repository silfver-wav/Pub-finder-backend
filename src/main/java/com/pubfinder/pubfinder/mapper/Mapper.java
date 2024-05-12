package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.models.Pub.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User.User;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

  Mapper INSTANCE = Mappers.getMapper(Mapper.class);

  PubDto entityToDto(Pub entity);

  UserDto entityToDto(User entity);

  ReviewDto entityToDto(Review entity);

  AdditionalInfoDto entityToDto(AdditionalInfo additionalInfo);

  Pub dtoToEntity(PubDto dto);

  User dtoToEntity(UserDto dto);

  Review dtoToEntity(ReviewDto dto);

  AdditionalInfo dtoToEntity(AdditionalInfoDto dto);
}