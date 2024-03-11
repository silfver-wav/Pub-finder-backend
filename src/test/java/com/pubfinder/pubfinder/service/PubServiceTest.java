package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.PubMapper;
import com.pubfinder.pubfinder.models.Pub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PubServiceTest {

    /*
    @Autowired PubsService pubsService;

    @MockBean
    private PubRepository pubRepository;

    @Test
    public void getPubTest() {
        UUID pubId = pub.getId();
        when(pubRepository.findById(any())).thenReturn(Optional.of(pub));
        ResponseEntity<PubDTO> result = pubsService.getPub(pubId);
        assertEquals(ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(pub)), result);
        verify(pubRepository, times(1)).findById(pubId);
    }

    @Test
    public void getPubTest_NOT_FOUND() {
        UUID pubId = pub.getId();
        when(pubRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<PubDTO> result = pubsService.getPub(pubId);
        assertEquals(ResponseEntity.notFound().build(), result);
        verify(pubRepository, times(1)).findById(pubId);
    }

    @Test
    public void getPubByNameTest() {
        String name = pub.getName();
        when(pubRepository.findByName(any())).thenReturn(Optional.of(pub));
        ResponseEntity<PubDTO> result = pubsService.getPubByName(name);
        assertEquals(ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(pub)), result);
        verify(pubRepository, times(1)).findByName(name);
    }

    @Test
    public void getPubByNameTest_NOT_FOUND() {
        String name = pub.getName();
        when(pubRepository.findByName(any())).thenReturn(Optional.empty());
        ResponseEntity<PubDTO> result = pubsService.getPubByName(name);
        assertEquals(ResponseEntity.notFound().build(), result);
        verify(pubRepository, times(1)).findByName(name);
    }

    @Test
    public void getPubsTest() {
        List<Pub> pubs = new ArrayList<>(Collections.singletonList(pub));
        when(pubRepository.findPubsWithInRadius(1.0,1.0,1)).thenReturn(pubs);
        ResponseEntity<List<PubDTO>> result = pubsService.getPubs(1.0,1.0,1.0);
        assertEquals( 1, Objects.requireNonNull(result.getBody()).size());
        verify(pubRepository, times(1)).findPubsWithInRadius(1.0,1.0,1.0);
    }

    @Test
    public void savePubTest() {
        when(pubRepository.save(any())).thenReturn(pub);
        ResponseEntity<PubDTO> result = pubsService.savePub(pub);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(PubMapper.INSTANCE.entityToDto(pub)), result);
        verify(pubRepository, times(1)).save(pub);
    }

    @Test
    public void savePubTest_BAD_REQUEST() {
        ResponseEntity<PubDTO> result = pubsService.savePub(null);
        assertEquals(ResponseEntity.badRequest().build(), result);
    }

    @Test
    public void editPubTest() {
        Pub updatedPub = new Pub(pub.getId(), "something else", pub.getLat(), pub.getLng(), pub.getOpen(), pub.getLocation(), pub.getDescription());
        when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));
        when(pubRepository.save(any())).thenReturn(updatedPub);
        ResponseEntity<PubDTO> result = pubsService.editPub(updatedPub);
        assertEquals(ResponseEntity.ok().body(PubMapper.INSTANCE.entityToDto(updatedPub)), result);
        verify(pubRepository, times(1)).save(updatedPub);
    }

    @Test
    public void editPubTest_BAD_REQUEST() {
        Pub updatedPub = new Pub(null, "something else", pub.getLat(), pub.getLng(), pub.getOpen(), pub.getLocation(), pub.getDescription());
        when(pubRepository.findById(pub.getId())).thenReturn(Optional.empty());
        ResponseEntity<PubDTO> result = pubsService.editPub(updatedPub);
        assertEquals(ResponseEntity.badRequest().build(), result);
    }

    @Test
    public void deletePubTest() {
        doNothing().when(pubRepository).delete(pub);
        ResponseEntity<PubDTO> result = pubsService.deletePub(pub);
        assertEquals(ResponseEntity.ok().build(), result);
        verify(pubRepository, times(1)).delete(any());
    }

    @Test
    public void deletePubTest_BAD_REQUEST() {
        ResponseEntity<PubDTO> result = pubsService.deletePub(null);
        assertEquals(ResponseEntity.badRequest().build(), result);
    }

    private final Pub pub = new Pub(UUID.randomUUID(), "name", 1.0, 1.0, "open", "location", "description");

     */
}
