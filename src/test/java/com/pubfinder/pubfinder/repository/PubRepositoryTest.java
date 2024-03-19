package com.pubfinder.pubfinder.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
})
public class PubRepositoryTest {

    @Autowired
    private PubRepository pubRepository;

    @Test
    public void saveAndGetPubTest() {
        Pub savedPub = pubRepository.save(TestUtil.generateMockPub());
        Optional<Pub> pub = pubRepository.findById(savedPub.getId());

        assertTrue(pub.isPresent());
        assertEquals(savedPub.getName(), pub.get().getName());
        assertEquals(savedPub.getLat(), pub.get().getLat());
        assertEquals(savedPub.getLng(), pub.get().getLng());
        assertEquals(savedPub.getOpeningHours(), pub.get().getOpeningHours());
        assertEquals(savedPub.getLocation(), pub.get().getLocation());
        assertEquals(savedPub.getDescription(), pub.get().getDescription());
    }

    @Test
    public void getPubByNameTest() {
        Pub savedPub = pubRepository.save(TestUtil.generateMockPub());
        Optional<Pub> pub = pubRepository.findByName(savedPub.getName());

        assertTrue(pub.isPresent());
        assertEquals(savedPub.getName(), pub.get().getName());
        assertEquals(savedPub.getLat(), pub.get().getLat());
        assertEquals(savedPub.getLng(), pub.get().getLng());
        assertEquals(savedPub.getOpeningHours(), pub.get().getOpeningHours());
        assertEquals(savedPub.getLocation(), pub.get().getLocation());
        assertEquals(savedPub.getDescription(), pub.get().getDescription());
    }

    @Test
    public void deletePubTest() {
        Pub savedPub = pubRepository.save(TestUtil.generateMockPub());
        pubRepository.delete(savedPub);
        Optional<Pub> pub = pubRepository.findById(savedPub.getId());
        assertTrue(pub.isEmpty());
    }

    @Test
    public void editPubTest() {
        Pub savedPub = pubRepository.save(TestUtil.generateMockPub());
        savedPub.setName("something else");
        Pub editedPub = pubRepository.save(savedPub);
        assertEquals(savedPub, editedPub);
    }

    @Test
    public void findPubsWithInRadiusTest() {
        Pub pub1 = pubRepository.save(new Pub(UUID.randomUUID(),"Pub1", 1.0, 1.0, TestUtil.generateMockOpeningHours(), "location", "description"));
        Pub pub2 = pubRepository.save(new Pub(UUID.randomUUID(),"Pub2", 1.01, 1.0, TestUtil.generateMockOpeningHours(), "location", "description"));
        Pub pub3 = pubRepository.save(new Pub(UUID.randomUUID(),"Pub3", 1.0, 1.03, TestUtil.generateMockOpeningHours(), "location", "description"));
        pubRepository.save(new Pub(UUID.randomUUID(),"Pub4", 1.01, 50.00, TestUtil.generateMockOpeningHours(), "location", "description"));
        pubRepository.save(new Pub(UUID.randomUUID(),"Pub5", 1.01, 50.01, TestUtil.generateMockOpeningHours(), "location", "description"));

        List<Pub> excepted = new ArrayList<>(Arrays.asList(pub1, pub2, pub3));
        List<Pub> filteredPubs = pubRepository.findPubsWithInRadius(1.0,1.0,5.0);

        assertEquals(filteredPubs, excepted);
    }

}
