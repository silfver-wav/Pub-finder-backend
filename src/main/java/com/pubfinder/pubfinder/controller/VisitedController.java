package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.VisitedDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.service.VisitedService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visited")
public class VisitedController {

  @Autowired
  private VisitedService visitedService;

  @PostMapping("/save/{pubId}/{username}")
  public ResponseEntity<VisitedDto> save(@PathVariable("pubId") UUID pubId,
      @PathVariable("username") String username) throws ResourceNotFoundException {
    visitedService.saveVisit(pubId, username);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws ResourceNotFoundException {
    visitedService.deleteVisit(id);
    return ResponseEntity.noContent().build();
  }
}
