package com.pubfinder.pubfinder.repository;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.db.UserVisitedPubRepository;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.UserVisitedPub;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
})
public class UserVisitedPubRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private UserVisitedPubRepository userVisitedPubRepository;

    @Test
    public void saveAndGetTest() {
        UserVisitedPub savedUVP = userVisitedPubRepository.save(uvp);
        Optional<UserVisitedPub> foundUVP = userVisitedPubRepository.findById(savedUVP.getId());

        assertTrue(foundUVP.isPresent());
        assertEquals(foundUVP.get(), savedUVP);
    }

    @Test
    public void saveAndGetByPubAndUserTest() {
        User user = userRepository.save(uvp.getUser());
        Pub pub = pubRepository.save(uvp.getPub());
        uvp.setUser(user);
        uvp.setPub(pub);

        UserVisitedPub savedUVP = userVisitedPubRepository.save(uvp);
        Optional<UserVisitedPub> foundUVP = userVisitedPubRepository.findByPubAndUser(pub, user);

        assertTrue(foundUVP.isPresent());
        assertEquals(foundUVP.get(), savedUVP);
    }

    @Test
    public void deleteAllByUserTest() {
        User user = userRepository.save(uvp.getUser());
        Pub pub1 = pubRepository.save(uvp.getPub());
        Pub pub2 = pubRepository.save(uvp.getPub());
        Pub pub3 = pubRepository.save(uvp.getPub());
        UserVisitedPub uvp1 = UserVisitedPub.builder()
                .user(user)
                .pub(pub1)
                .visitedDate(LocalDateTime.now())
                .build();

        UserVisitedPub uvp2 = UserVisitedPub.builder()
                .user(user)
                .pub(pub2)
                .visitedDate(LocalDateTime.now())
                .build();

        UserVisitedPub uvp3 = UserVisitedPub.builder()
                .user(user)
                .pub(pub3)
                .visitedDate(LocalDateTime.now())
                .build();

        userVisitedPubRepository.saveAll(Arrays.asList(uvp1, uvp2, uvp3));

        List<UserVisitedPub> uvps1 = userVisitedPubRepository.findAllByUser(user);
        assertEquals(3, uvps1.size());

        userVisitedPubRepository.deleteAllByUser(user);

        List<UserVisitedPub> uvps2 = userVisitedPubRepository.findAllByUser(user);
        assertEquals(0, uvps2.size());
    }

    @Test
    public void deleteTest() {
        UserVisitedPub savedUVP = userVisitedPubRepository.save(uvp);
        userVisitedPubRepository.delete(savedUVP);
        Optional<UserVisitedPub> foundUVP = userVisitedPubRepository.findById(savedUVP.getId());
        assertTrue(foundUVP.isEmpty());
    }

    @Test
    public void editTest() {
        UserVisitedPub savedUVP = userVisitedPubRepository.save(uvp);
        savedUVP.setVisitedDate(LocalDateTime.now());
        UserVisitedPub editedUVP = userVisitedPubRepository.save(uvp);
        assertEquals(savedUVP, editedUVP);
    }

    private final UserVisitedPub uvp = TestUtil.generateUserVisitedPub();
}
