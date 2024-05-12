package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.service.PubsService;
import java.util.List;
import java.util.UUID;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Pubs controller.
 */
@RestController
@RequestMapping("/pub")
public class PubsController {

  @Autowired
  private PubsService pubsService;

  @GetMapping(value = "/getPubs/{lat}/{lng}/{radius}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<List<PubDto>> getPubs(@PathVariable("lat") Double lat,
      @PathVariable("lng") Double lng, @PathVariable("radius") Double radius) {
    return ResponseEntity.ok().body(pubsService.getPubs(lat, lng, radius));
  }

  @GetMapping("/getPub/{id}")
  public ResponseEntity<PubDto> getPub(@PathVariable("id") UUID id)
      throws ResourceNotFoundException {
    Pub pub = pubsService.getPub(id);
    return ResponseEntity.ok(Mapper.INSTANCE.entityToDto(pub));
  }

  @GetMapping("/searchPubs/{term}")
  public ResponseEntity<List<PubDto>> searchForPubs(@PathVariable("term") String term) {
    return ResponseEntity.ok(pubsService.searchPubsByTerm(term));
  }

  @PostMapping("/createPub")
  public ResponseEntity<PubDto> save(@RequestBody PubDto pub) throws BadRequestException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(pubsService.save(Mapper.INSTANCE.dtoToEntity(pub)));
  }

  @PutMapping("/editPub")
  public ResponseEntity<PubDto> edit(@RequestBody PubDto pub)
      throws ResourceNotFoundException, BadRequestException {
    return ResponseEntity.ok().body(pubsService.edit(Mapper.INSTANCE.dtoToEntity(pub)));
  }

  @DeleteMapping("/deletePub")
  public ResponseEntity<Void> delete(@RequestBody PubDto pub) throws BadRequestException {
    pubsService.delete(Mapper.INSTANCE.dtoToEntity(pub));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/reviews/{id}")
  public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(pubsService.getReviews(id));
  }
}
