package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PubsService {

    @Autowired
    private PubRepository pubRepository;

    @Cacheable(value = "getPub")
    public PubDTO getPub(UUID id) throws ResourceNotFoundException {
        return pubRepository.findById(id).map(Mapper.INSTANCE::entityToDto).orElseThrow(() -> new ResourceNotFoundException("Pub with id " + id + " was not found"));
    }

    public PubDTO getPubByName(String name) throws ResourceNotFoundException {
        return pubRepository.findByName(name).map(Mapper.INSTANCE::entityToDto).orElseThrow(() -> new ResourceNotFoundException("Pub with name " + name + " was not found"));
    }

    @Cacheable(value = "getPubs",
            condition = "#radius <= 10",
            key = "#lat.toString().substring(0, 5) + '-' + #lng.toString().substring(0, 5) + '-' + #radius.toString()")
    public List<PubDTO> getPubs(Double lat, Double lng, Double radius) {
        return pubRepository.findPubsWithInRadius( lat, lng, radius).stream().map(Mapper.INSTANCE::entityToDto).toList();
    }

    public PubDTO savePub(Pub pub) throws BadRequestException {
        if (pub == null) throw new BadRequestException();
        Pub savedPub = pubRepository.save(pub);
        return Mapper.INSTANCE.entityToDto(savedPub);
    }

    public PubDTO editPub(Pub pub) throws ResourceNotFoundException, BadRequestException {
        if (pub == null || pub.getId() == null) throw new BadRequestException();

        Optional<Pub> foundPub = pubRepository.findById(pub.getId());
        if (foundPub.isEmpty()) throw new ResourceNotFoundException("Pub with id " + pub.getId() + " was not found");

        Pub savedPub = pubRepository.save(pub);
        return Mapper.INSTANCE.entityToDto(savedPub);
    }

    public void deletePub(Pub pub) throws BadRequestException {
        if (pub == null) throw new BadRequestException();
        pubRepository.delete(pub);
    }
}
