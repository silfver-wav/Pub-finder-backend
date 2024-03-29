package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PubsService {

    @Autowired
    private PubRepository pubRepository;

    @Cacheable(value = "getPub")
    public PubDTO getPub(UUID id) throws ResourceNotFoundException {
        return pubRepository.findById(id).map(Mapper.INSTANCE::entityToDto).orElseThrow(() -> new ResourceNotFoundException("Pub with id " + id + " was not found"));
    }

    // Note stop names (a, an, the, of etc) should not be sent to the backend, unless it's the unless it is the first word of the name
    // And the prefix should be max length to 8, the average cardinality will be increased
    @Cacheable(value = "getPubsByTerm",
            condition = "#term.length() > 1 && #term.length() < 9",
            key = "#term"
    )
    public List<PubDTO> searchPubsByTerm(String term) {
        List<Object[]> pubs = pubRepository.findPubsByNameContaining(term);
        List<PubDTO> pubsList = new ArrayList<>();
        for (Object[] pub : pubs) {
            pubsList.add(PubDTO.builder().id((UUID) pub[0]).name((String) pub[1]).build());
        }
        return pubsList;
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
