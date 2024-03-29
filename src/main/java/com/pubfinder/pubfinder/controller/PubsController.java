package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.PubsService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pub")
public class PubsController {

    @Autowired
    private PubsService pubsService;

    @GetMapping("/test")
    public ResponseEntity<String> getPubs() {
        return ResponseEntity.ok().body("hello world");
    }

    @GetMapping("/getPubs/{lat}/{lng}/{radius}")
    public ResponseEntity<List<PubDTO>> getPubs(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng, @PathVariable("radius") Double radius) {
        return ResponseEntity.ok().body(pubsService.getPubs(lat, lng, radius));
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
}
