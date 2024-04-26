package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.ReviewDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.service.PubsService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pub")
public class PubsController {

    @Autowired
    private PubsService pubsService;

    @GetMapping("/test")
    public ResponseEntity<String> getPubs() {
        return ResponseEntity.ok().body("hello world");
    }

    @GetMapping(value = "/getPubs/{lat}/{lng}/{radius}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<PubDTO>> getPubs(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng, @PathVariable("radius") Double radius) {
        return ResponseEntity.ok().body(pubsService.getPubs(lat, lng, radius));
    }

    @GetMapping("/getPub/{id}")
    public ResponseEntity<PubDTO> searchForPubs(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Pub pub = pubsService.getPub(id);
        return ResponseEntity.ok(Mapper.INSTANCE.entityToDto(pub));
    }

    @GetMapping("/searchPubs/{term}")
    public ResponseEntity<List<PubDTO>> searchForPubs(@PathVariable("term") String term) {
        return ResponseEntity.ok(pubsService.searchPubsByTerm(term));
    }

    @PostMapping("/createPub")
    public ResponseEntity<PubDTO> createPub(@RequestBody PubDTO pub) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(pubsService.savePub(Mapper.INSTANCE.dtoToEntity(pub)));
    }

    @PutMapping("/editPub")
    public ResponseEntity<PubDTO> editPub(@RequestBody PubDTO pub) throws ResourceNotFoundException, BadRequestException {
        return ResponseEntity.ok().body(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub)));
    }

    @DeleteMapping("/deletePub")
    public ResponseEntity<Void> deletePub(@RequestBody PubDTO pub) throws BadRequestException {
        pubsService.deletePub(Mapper.INSTANCE.dtoToEntity(pub));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/visited/{pubId}/{username}")
    public ResponseEntity<Void> visitedPub(@PathVariable UUID pubId, @PathVariable String username) throws ResourceNotFoundException {
        pubsService.visitPub(pubId, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/visited/{pubId}/{username}")
    public ResponseEntity<Void> removeVisitedPub(@PathVariable UUID pubId, @PathVariable String username) throws ResourceNotFoundException {
        pubsService.removeVisitedPub(pubId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(pubsService.getReviews(id));
    }
}
