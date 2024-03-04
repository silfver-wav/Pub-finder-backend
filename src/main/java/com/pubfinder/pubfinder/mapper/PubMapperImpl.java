package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.Pub;

public class PubMapperImpl implements PubMapper {

    @Override
    public Pub dtoToEntity(PubDTO dto) {
        if (dto == null) return null;
        if (dto.getId() == null)
        return new Pub(dto.getName(), dto.getGeocode(), dto.getOpen(), dto.getLocation(), dto.getDescription());
        else return new Pub(dto.getName(), dto.getGeocode(), dto.getOpen(), dto.getLocation(), dto.getDescription());
    }

    @Override
    public PubDTO entityToDto(Pub entity) {
        if (entity == null) return null;
        return new PubDTO(entity.getId(), entity.getName(), entity.getGeocode(), entity.getOpen(), entity.getLocation(), entity.getDescription());
    }
}
