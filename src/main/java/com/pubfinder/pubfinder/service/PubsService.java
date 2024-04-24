package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.db.UserVisitedPubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.UserVisitedPub;
import com.pubfinder.pubfinder.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PubsService {

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserVisitedPubRepository userVisitedPubRepository;

    @Cacheable(value = "getPub")
    public PubDTO getPub(UUID id) throws ResourceNotFoundException {
        return pubRepository.findById(id).map(Mapper.INSTANCE::entityToDto).orElseThrow(() -> new ResourceNotFoundException("Pub with id " + id + " was not found"));
    }

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

    public void visitPub(UUID pubId, String username) throws ResourceNotFoundException {
        Optional<Pub> pub = pubRepository.findById(pubId);
        if (pub.isEmpty()) throw new ResourceNotFoundException("Pub with id: " + pubId + " not found");

        User user = userService.getUser(username);

        Optional<UserVisitedPub> uvp = userVisitedPubRepository.findByPubAndUser(pub.get(), user);
        UserVisitedPub userVisitedPub;
        if (uvp.isPresent()) {
            userVisitedPub = uvp.get();
            userVisitedPub.setVisitedDate(LocalDateTime.now());
        } else {
            userVisitedPub = UserVisitedPub.builder().user(user).pub(pub.get()).visitedDate(LocalDateTime.now()).build();
        }
        userVisitedPubRepository.save(userVisitedPub);
    }

    public void removeVisitedPub(UUID pubId, String username) throws ResourceNotFoundException {
        Optional<Pub> pub = pubRepository.findById(pubId);
        if (pub.isEmpty()) throw new ResourceNotFoundException("Pub with id: " + pubId + " not found");

        User user = userService.getUser(username);

        Optional<UserVisitedPub> uvp = userVisitedPubRepository.findByPubAndUser(pub.get(), user);
        if (uvp.isPresent()) {
            userVisitedPubRepository.delete(uvp.get());
        } else {
            throw new ResourceNotFoundException("User Visited Pub with pub: " + pubId + " and user: " + user.getId() + " not found");
        }
    }
}
