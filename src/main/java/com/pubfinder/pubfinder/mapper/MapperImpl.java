package com.pubfinder.pubfinder.mapper;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MapperImpl implements Mapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Pub dtoToEntity(PubDTO dto) {
        if (dto == null) return null;

        Pub.PubBuilder builder = Pub.builder()
                .name(dto.getName())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .openingHours(dto.getOpeningHours())
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
                .openingHours(entity.getOpeningHours())
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
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();
    }
}
