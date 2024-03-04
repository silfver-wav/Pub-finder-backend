package com.pubfinder.pubfinder.repository;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.models.Pub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PubRepositoryTest {

    @Autowired
    private PubRepository pubRepository;

    @Test
    public void savePubTest() {
        Pub pub = new Pub("name", 1.01, 1.01, "open", "location", "description");
        Pub result = pubRepository.save(pub);
        assertEquals(result.getName(), pub.getName());
        assertEquals(result.getLat(), pub.getLat());
        assertEquals(result.getLng(), pub.getLng());
        assertEquals(result.getOpen(), pub.getOpen());
        assertEquals(result.getLocation(), pub.getLocation());
        assertEquals(result.getDescription(), pub.getDescription());
    }

    @Test
    public void getPubTest() {
        Pub savedPub = pubRepository.save(new Pub("name", 1.01, 1.01, "open", "location", "description"));
        Optional<Pub> pub = pubRepository.findById(savedPub.getId());

        assertTrue(pub.isPresent());
        assertEquals(savedPub.getName(), pub.get().getName());
        assertEquals(savedPub.getLat(), pub.get().getLat());
        assertEquals(savedPub.getLng(), pub.get().getLng());
        assertEquals(savedPub.getOpen(), pub.get().getOpen());
        assertEquals(savedPub.getLocation(), pub.get().getLocation());
        assertEquals(savedPub.getDescription(), pub.get().getDescription());
    }

    @Test
    public void getPubByNameTest() {
        Pub savedPub = pubRepository.save(new Pub("name", 1.01, 1.01, "open", "location", "description"));
        Optional<Pub> pub = pubRepository.findByName(savedPub.getName());

        assertTrue(pub.isPresent());
        assertEquals(savedPub.getName(), pub.get().getName());
        assertEquals(savedPub.getLat(), pub.get().getLat());
        assertEquals(savedPub.getLng(), pub.get().getLng());
        assertEquals(savedPub.getOpen(), pub.get().getOpen());
        assertEquals(savedPub.getLocation(), pub.get().getLocation());
        assertEquals(savedPub.getDescription(), pub.get().getDescription());
    }

    @Test
    public void deletePubTest() {
        Pub savedPub = pubRepository.save(new Pub("name", 1.01, 1.01, "open", "location", "description"));
        pubRepository.delete(savedPub);
        Optional<Pub> pub = pubRepository.findById(savedPub.getId());
        assertTrue(pub.isEmpty());
    }

    @Test
    public void editPubTest() {
        Pub savedPub = pubRepository.save(new Pub("name", 1.01, 1.01, "open", "location", "description"));
        savedPub.setName("something else");
        Pub editedPub = pubRepository.save(savedPub);
        assertEquals(savedPub, editedPub);
    }

    @Test
    public void findPubsWithInRadiusTest() {
        Pub pub1 = pubRepository.save(new Pub("Pub1", 1.0, 1.0, "open", "location", "description"));
        Pub pub2 = pubRepository.save(new Pub("Pub2", 1.01, 1.0, "open", "location", "description"));
        Pub pub3 = pubRepository.save(new Pub("Pub3", 1.0, 1.03, "open", "location", "description"));
        pubRepository.save(new Pub("Pub4", 1.01, 50.00, "open", "location", "description"));
        pubRepository.save(new Pub("Pub5", 1.01, 50.01, "open", "location", "description"));

        List<Pub> excepted = new ArrayList<>(Arrays.asList(pub1, pub2, pub3));
        List<Pub> filteredPubs = pubRepository.findPubsWithInRadius(1.0,1.0,5.0);

        assertEquals(filteredPubs, excepted);
    }

}
