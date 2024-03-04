package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.Pub;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PubMapper {

    PubMapper INSTANCE = Mappers.getMapper( PubMapper.class );
    @Mapping(target = "id", ignore = true)
    Pub dtoToEntity(PubDTO dto);

    PubDTO entityToDto(Pub entity);
}
