package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.PubMapper;
import com.pubfinder.pubfinder.models.Pub;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<PubDTO> getPub(UUID id) {
        return pubRepository.findById(id).map(pub -> ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(pub))).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<PubDTO> getPubByName(String name) {
        return pubRepository.findByName(name).map(pub -> ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(pub))).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<PubDTO>> getPubs(Double lat, Double lng, int radius) {
        List<PubDTO> pubs = pubRepository.filterByLocation(lat.toString(), lng.toString(),radius).stream().map(PubMapper.INSTANCE::entityToDto).toList();
        return ResponseEntity.ok().body(pubs);
    }

    public ResponseEntity<PubDTO> savePub(Pub pub) {
        if (pub == null) return ResponseEntity.badRequest().build();
        Pub savedPub = pubRepository.save(pub);
        return ResponseEntity.status(HttpStatus.CREATED).body(PubMapper.INSTANCE.entityToDto(savedPub));
    }

    public ResponseEntity<PubDTO> editPub(Pub pub) {
        if (pub == null || pub.getId() == null) return ResponseEntity.badRequest().build();

        Optional<Pub> foundPub = pubRepository.findById(pub.getId());
        if (foundPub.isEmpty()) return ResponseEntity.notFound().build();

        Pub savedPub = pubRepository.save(pub);
        return ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(savedPub));
    }

    public ResponseEntity<PubDTO> deletePub(Pub pub) {
        if (pub == null) return ResponseEntity.badRequest().build();
        pubRepository.delete(pub);
        return ResponseEntity.ok().build();
    }

}
