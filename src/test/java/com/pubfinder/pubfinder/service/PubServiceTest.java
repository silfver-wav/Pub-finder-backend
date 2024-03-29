package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.util.TestUtil;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.cache.type=none",
        "bucket4j.enabled=false",
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
public class PubServiceTest {

    @Autowired PubsService pubsService;

    @MockBean
    private PubRepository pubRepository;

    @Test
    public void getPubTest() throws ResourceNotFoundException {
        UUID pubId = pub.getId();
        when(pubRepository.findById(any())).thenReturn(Optional.of(pub));
        PubDTO result = pubsService.getPub(pubId);
        assertEquals(Mapper.INSTANCE.entityToDto(pub), result);
        verify(pubRepository, times(1)).findById(pubId);
    }

    @Test
    public void getPubTest_NOT_FOUND() {
        UUID pubId = pub.getId();
        when(pubRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> pubsService.getPub(pubId));
        verify(pubRepository, times(1)).findById(pubId);
    }

    @Test
    public void searchPubsByTermTest() throws ResourceNotFoundException {
        PubDTO bigBen = PubDTO.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
        PubDTO liffey = PubDTO.builder().id(UUID.randomUUID()).name("The Liffey").build();
        List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()}, new Object[]{liffey.getId(), liffey.getName()});

        when(pubRepository.findPubsByNameContaining(any())).thenReturn(dbRs);
        List<PubDTO> result = pubsService.searchPubsByTerm("The");
        assertEquals(List.of(bigBen, liffey), result);
        verify(pubRepository, times(1)).findPubsByNameContaining("The");
    }

    @Test
    public void getPubsTest() {
        List<Pub> pubs = new ArrayList<>(TestUtil.generateListOfMockPubs());
        when(pubRepository.findPubsWithInRadius(40.712810, 74.006010,1)).thenReturn(pubs);
        List<PubDTO> result = pubsService.getPubs(40.712810, 74.006010,1.0);
        assertEquals( 3, result.size());
        verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010,1);
    }

    @Test
    public void savePubTest() throws BadRequestException {
        when(pubRepository.save(any())).thenReturn(pub);
        PubDTO result = pubsService.savePub(pub);
        assertEquals(Mapper.INSTANCE.entityToDto(pub), result);
        verify(pubRepository, times(1)).save(pub);
    }

    @Test
    public void savePubTest_BAD_REQUEST() {
        assertThrows( BadRequestException.class ,() -> pubsService.savePub(null));
    }

    @Test
    public void editPubTest() throws BadRequestException, ResourceNotFoundException {
        Pub updatedPub = new Pub(pub.getId(), "something else", pub.getLat(), pub.getLng(), pub.getOpeningHours(), pub.getLocation(), pub.getDescription());
        when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));
        when(pubRepository.save(any())).thenReturn(updatedPub);
        PubDTO result = pubsService.editPub(updatedPub);
        assertEquals(Mapper.INSTANCE.entityToDto(updatedPub), result);
        verify(pubRepository, times(1)).save(updatedPub);
    }

    @Test
    public void editPubTest_BAD_REQUEST() {
        Pub updatedPub = new Pub(null, "something else", pub.getLat(), pub.getLng(), pub.getOpeningHours(), pub.getLocation(), pub.getDescription());
        when(pubRepository.findById(pub.getId())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> pubsService.editPub(updatedPub));
    }

    @Test
    public void deletePubTest() throws BadRequestException {
        doNothing().when(pubRepository).delete(pub);
        pubsService.deletePub(pub);
        verify(pubRepository, times(1)).delete(any());
    }

    @Test
    public void deletePubTest_BAD_REQUEST() {
        assertThrows(BadRequestException.class, () -> pubsService.deletePub(null));
    }

    private final Pub pub = TestUtil.generateMockPub();
}
