package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;

public class MapperImpl implements Mapper {

    @Override
    public Pub dtoToEntity(PubDTO dto) {
        if (dto == null) return null;

        Pub.PubBuilder builder = Pub.builder()
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .open(dto.getOpen())
                .location(dto.getLocation())
                .description(dto.getDescription());

        if (dto.getId() != null) {
            builder.id(dto.getId());
        }

        return builder.build();
    }

    @Override
    public PubDTO entityToDto(Pub entity) {
        if (entity == null) return null;
        return PubDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lat(entity.getLat())
                .lng(entity.getLng())
                .open(entity.getOpen())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public UserDTO entityToDto(User entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .username(entity.getUsername())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

    @Override
    public User dtoToEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
