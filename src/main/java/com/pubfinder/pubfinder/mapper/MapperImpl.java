package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.User;

public class MapperImpl implements Mapper {


    @Override
    public Pub dtoToEntity(PubDTO dto) {
        if (dto == null) return null;

        Pub.PubBuilder builder = Pub.builder()
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .openingHours(dto.getOpeningHours())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .accessibility(dto.getAccessibility())
                .price(dto.getPrice())
                .website(dto.getWebsite())
                .washroom(dto.getWashroom())
                .outDoorSeating(dto.getOutDoorSeating());

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
                .openingHours(entity.getOpeningHours())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .accessibility(entity.getAccessibility())
                .price(entity.getPrice())
                .website(entity.getWebsite())
                .washroom(entity.getWashroom())
                .outDoorSeating(entity.getOutDoorSeating())
                .build();
    }

    @Override
    public UserDTO entityToDto(User entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .id(entity.getId())
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
                .id(dto.getId())
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.ADMIN)
                .build();
    }
}
