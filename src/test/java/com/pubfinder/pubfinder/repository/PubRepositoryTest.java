package com.pubfinder.pubfinder.repository;

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
        Optional<Pub> foundPub = pubRepository.findById(savedPub.getId());

        assertTrue(foundPub.isPresent());
        assertEquals(savedPub.getName(), foundPub.get().getName());
        assertEquals(savedPub.getLat(), foundPub.get().getLat());
        assertEquals(savedPub.getLng(), foundPub.get().getLng());
        assertEquals(savedPub.getOpeningHours(), foundPub.get().getOpeningHours());
        assertEquals(savedPub.getLocation(), foundPub.get().getLocation());
        assertEquals(savedPub.getDescription(), foundPub.get().getDescription());
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
        Pub pub1 = pubRepository.save(
                    Pub.builder()
                            .id(UUID.randomUUID())
                            .name("Pub1")
                            .lat(1.0)
                            .lng(1.0)
                            .openingHours(TestUtil.generateMockOpeningHours())
                            .location("location")
                            .description("description")
                            .price("$")
                            .website("google.com")
                            .outDoorSeating(true)
                            .washroom(true)
                            .accessibility(TestUtil.generateMockAccessibility())
                            .build()
        );
        Pub pub2 = pubRepository.save(
                Pub.builder()
                        .id(UUID.randomUUID())
                        .name("Pub2")
                        .lat(1.01)
                        .lng(1.0)
                        .openingHours(TestUtil.generateMockOpeningHours())
                        .location("location")
                        .description("description")
                        .price("$")
                        .website("google.com")
                        .outDoorSeating(true)
                        .washroom(true)
                        .accessibility(TestUtil.generateMockAccessibility())
                        .build()
        );
        Pub pub3 = pubRepository.save(
                Pub.builder()
                        .id(UUID.randomUUID())
                        .name("Pub3")
                        .lat(1.0)
                        .lng(1.03)
                        .openingHours(TestUtil.generateMockOpeningHours())
                        .location("location")
                        .description("description")
                        .price("$")
                        .website("google.com")
                        .outDoorSeating(true)
                        .washroom(true)
                        .accessibility(TestUtil.generateMockAccessibility())
                        .build()
        );

        pubRepository.save(
            Pub.builder()
                .id(UUID.randomUUID())
                .name("Pub4")
                .lat(1.01)
                .lng(50.00)
                .openingHours(TestUtil.generateMockOpeningHours())
                .location("location")
                .description("description")
                .price("$")
                .website("google.com")
                .outDoorSeating(true)
                .washroom(true)
                .accessibility(TestUtil.generateMockAccessibility())
                .build()
        );
        pubRepository.save(
                Pub.builder()
                        .id(UUID.randomUUID())
                        .name("Pub5")
                        .lat(1.01)
                        .lng(50.01)
                        .openingHours(TestUtil.generateMockOpeningHours())
                        .location("location")
                        .description("description")
                        .price("$")
                        .website("google.com")
                        .outDoorSeating(true)
                        .washroom(true)
                        .accessibility(TestUtil.generateMockAccessibility())
                        .build()
        );


        List<Pub> excepted = new ArrayList<>(Arrays.asList(pub1, pub2, pub3));
        List<Pub> filteredPubs = pubRepository.findPubsWithInRadius(1.0,1.0,5.0);

        assertEquals(filteredPubs, excepted);
    }

    @Test
    public void findMoviesByNameContainingTest() {
        Pub bigBen = TestUtil.generateMockPub();
        bigBen.setName("The Big Ben Pub");

        Pub liffey = TestUtil.generateMockPub();
        liffey.setName("The Liffey");

        Pub connell = TestUtil.generateMockPub();
        connell.setName("O’Connell’s Irish Pub");

        Pub queens = TestUtil.generateMockPub();
        queens.setName("The Queen's Head");

        pubRepository.save(bigBen);
        pubRepository.save(liffey);
        pubRepository.save(connell);
        pubRepository.save(queens);

        List<Object[]> foundThePubs = pubRepository.findPubsByNameContaining("The");
        assertEquals(foundThePubs.size(), 3);
        assertEquals(foundThePubs.get(0)[1], bigBen.getName());
        assertEquals(foundThePubs.get(1)[1], liffey.getName());
        assertEquals(foundThePubs.get(2)[1], queens.getName());

        List<Object[]> foundPubPubs = pubRepository.findPubsByNameContaining("Pub");
        assertEquals(foundPubPubs.size(), 2);
        assertEquals(foundPubPubs.get(0)[1], bigBen.getName());
        assertEquals(foundPubPubs.get(1)[1], connell.getName());
    }
}
