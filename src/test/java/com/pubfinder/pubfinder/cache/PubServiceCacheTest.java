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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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

        PubDTO result1 = pubsService.getPub(pub.getId());
        PubDTO result2 = pubsService.getPub(pub.getId());

        assertEquals(result1, result2);

        verify(pubRepository, times(1)).findById(pub.getId());
    }
}
