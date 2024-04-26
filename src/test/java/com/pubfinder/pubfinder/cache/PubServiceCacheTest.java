package com.pubfinder.pubfinder.cache;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.service.PubsService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
public class PubServiceCacheTest {

    @Autowired
    private PubsService pubsService;
    @MockBean
    private PubRepository pubRepository;
    @Autowired
    private CacheManager cacheManager;

    // Tests if cache activates for similar locations
    @Test
    public void testGetPubs_CacheHit() {
        double radius = 2.0;

        when(pubRepository.findPubsWithInRadius(40.712810, 74.006010, radius)).thenReturn(TestUtil.generateListOfMockPubs());
        when(pubRepository.findPubsWithInRadius(40.712811, 74.006011, radius)).thenReturn(TestUtil.generateListOfMockPubs());

        List<PubDTO> response1 = pubsService.getPubs(40.712810, 74.006010, radius);
        List<PubDTO> response2 = pubsService.getPubs(40.712811, 74.006011, radius);

        assertEquals(response1, response2);

        verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010, radius);
        verify(pubRepository, times(0)).findPubsWithInRadius(40.712811, 74.006011, radius);
    }

    @Test
    public void testGetPubs_CacheMiss() {
        double radius = 15.0;

        when(pubRepository.findPubsWithInRadius(40.712810, 74.006010, radius)).thenReturn(TestUtil.generateListOfMockPubs());
        when(pubRepository.findPubsWithInRadius(40.712811, 74.006011, radius)).thenReturn(TestUtil.generateListOfMockPubs());

        List<PubDTO> response1 = pubsService.getPubs(40.712810, 74.006010, radius);
        List<PubDTO> response2 = pubsService.getPubs(40.712811, 74.006011, radius);

        verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010, radius);
        verify(pubRepository, times(1)).findPubsWithInRadius(40.712811, 74.006011, radius);
    }

    @Test
    public void testGetPub_CacheHit() throws ResourceNotFoundException {
        Pub pub = TestUtil.generateMockPub();

        when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));

        Pub result1 = pubsService.getPub(pub.getId());
        Pub result2 = pubsService.getPub(pub.getId());

        assertEquals(result1, result2);

        verify(pubRepository, times(1)).findById(pub.getId());
    }

    @Test
    public void testSearchPubsByTerm_CacheHit() {
        PubDTO bigBen = PubDTO.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
        PubDTO liffey = PubDTO.builder().id(UUID.randomUUID()).name("The Liffey").build();
        List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()}, new Object[]{liffey.getId(), liffey.getName()});

        when(pubRepository.findPubsByNameContaining("The")).thenReturn(dbRs);

        List<PubDTO> result1 = pubsService.searchPubsByTerm("The");
        List<PubDTO> result2 = pubsService.searchPubsByTerm("The");

        assertEquals(result1, result2);

        verify(pubRepository, times(1)).findPubsByNameContaining("The");
    }

    @Test
    public void testSearchPubsByTerm_CacheMissToSmall() {
        PubDTO bigBen = PubDTO.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
        PubDTO liffey = PubDTO.builder().id(UUID.randomUUID()).name("The Liffey").build();
        List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()}, new Object[]{liffey.getId(), liffey.getName()});

        when(pubRepository.findPubsByNameContaining("T")).thenReturn(dbRs);

        List<PubDTO> result1 = pubsService.searchPubsByTerm("T");
        List<PubDTO> result2 = pubsService.searchPubsByTerm("T");

        assertEquals(result1, result2);

        verify(pubRepository, times(2)).findPubsByNameContaining("T");
    }

    @Test
    public void testSearchPubsByTerm_CacheMissToBig() {
        PubDTO bigBen = PubDTO.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
        List<Object[]> dbRs = Collections.singletonList(new Object[]{bigBen.getId(), bigBen.getName()});

        when(pubRepository.findPubsByNameContaining("The Big Ben ")).thenReturn(dbRs);

        List<PubDTO> result1 = pubsService.searchPubsByTerm("The Big Ben ");
        List<PubDTO> result2 = pubsService.searchPubsByTerm("The Big Ben ");

        assertEquals(result1, result2);

        verify(pubRepository, times(2)).findPubsByNameContaining("The Big Ben ");
    }

}
