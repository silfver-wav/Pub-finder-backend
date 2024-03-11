package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.PubMapper;
import com.pubfinder.pubfinder.service.PubsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PubsController {

    @Autowired
    private PubsService pubsService;

    @GetMapping("/test")
    public ResponseEntity<String> getPubs() {
        return ResponseEntity.ok().body("hello world");
    }

    @GetMapping("/getPubs/{lat}/{lng}/{radius}")
    public ResponseEntity<List<PubDTO>> getPubs(@PathVariable("lat") Double lat, @PathVariable("lng") Double lng, @PathVariable("radius") Double radius) {
        return pubsService.getPubs(lat, lng, radius);
    }

    @GetMapping("/getPub/{name}")
    public ResponseEntity<PubDTO> getPub(@PathVariable("name") String name) {
        return pubsService.getPubByName(name);
    }

    @PostMapping("/savePub")
    public ResponseEntity<PubDTO> savePub(@RequestBody PubDTO pub) {
        return pubsService.savePub(PubMapper.INSTANCE.dtoToEntity(pub));
    }

    @PutMapping("/editPub")
    public ResponseEntity<PubDTO> editPub(@RequestBody PubDTO pub) {
        return pubsService.editPub(PubMapper.INSTANCE.dtoToEntity(pub));
    }

    @DeleteMapping("/deletePub")
    public ResponseEntity<PubDTO> deletePub(@RequestBody PubDTO pub) {
        return pubsService.deletePub(PubMapper.INSTANCE.dtoToEntity(pub));
    }

}
