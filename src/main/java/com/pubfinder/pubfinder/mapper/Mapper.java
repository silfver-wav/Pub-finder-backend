package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

  Mapper INSTANCE = Mappers.getMapper(Mapper.class);

  PubDto entityToDto(Pub entity);

  UserDto entityToDto(User entity);

  ReviewDto entityToDto(Review entity);

  Pub dtoToEntity(PubDto dto);

  User dtoToEntity(UserDto dto);

  Review dtoToEntity(ReviewDto dto);
}