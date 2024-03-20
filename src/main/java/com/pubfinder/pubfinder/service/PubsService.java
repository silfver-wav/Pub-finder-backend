package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
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
    public PubDTO getPub(UUID id) {
        return pubRepository.findById(id).map(Mapper.INSTANCE::entityToDto).orElseThrow();
    }

    public ResponseEntity<PubDTO> getPubByName(String name) {
        return pubRepository.findByName(name).map(pub -> ResponseEntity.ok().body(Mapper.INSTANCE.entityToDto(pub))).orElse(ResponseEntity.notFound().build());
    }

    @Cacheable(value = "getPubs",
            condition = "#radius <= 10",
            key = "#lat.toString().substring(0, 5) + '-' + #lng.toString().substring(0, 5) + '-' + #radius.toString()")
    public List<PubDTO> getPubs(Double lat, Double lng, Double radius) {
        return pubRepository.findPubsWithInRadius( lat, lng, radius).stream().map(Mapper.INSTANCE::entityToDto).toList();
    }

    public ResponseEntity<PubDTO> savePub(Pub pub) {
        if (pub == null) return ResponseEntity.badRequest().build();
        Pub savedPub = pubRepository.save(pub);
        return ResponseEntity.status(HttpStatus.CREATED).body(Mapper.INSTANCE.entityToDto(savedPub));
    }

    public ResponseEntity<PubDTO> editPub(Pub pub) {
        if (pub == null || pub.getId() == null) return ResponseEntity.badRequest().build();

        Optional<Pub> foundPub = pubRepository.findById(pub.getId());
        if (foundPub.isEmpty()) return ResponseEntity.notFound().build();

        Pub savedPub = pubRepository.save(pub);
        return ResponseEntity.ok().body(Mapper.INSTANCE.entityToDto(savedPub));
    }

    public ResponseEntity<PubDTO> deletePub(Pub pub) {
        if (pub == null) return ResponseEntity.badRequest().build();
        pubRepository.delete(pub);
        return ResponseEntity.ok().build();
    }

}
