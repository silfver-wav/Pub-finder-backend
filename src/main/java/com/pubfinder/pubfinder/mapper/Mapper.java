package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.ReviewDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.User;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface Mapper {

    Mapper INSTANCE = Mappers.getMapper( Mapper.class );

    Pub dtoToEntity(PubDTO dto);
    PubDTO entityToDto(Pub entity);
    UserDTO entityToDto(User entity);
    User dtoToEntity(UserDTO dto);
    ReviewDTO entityToDto(Review entity);
    Review dtoToEntity(ReviewDTO dto);
}