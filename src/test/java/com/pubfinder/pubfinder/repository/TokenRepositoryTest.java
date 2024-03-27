package com.pubfinder.pubfinder.repository;

import com.pubfinder.pubfinder.db.TokenRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.test.database.replace=none",
        "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"
})
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndGetTokenTest() {
        User user = userRepository.save(TestUtil.generateUser());
        Token savedToken = tokenRepository.save(TestUtil.generateToken(user));
        Optional<Token> foundToken = tokenRepository.findByToken(savedToken.getToken());

        assertTrue(foundToken.isPresent());
        assertEquals(savedToken.getToken(), foundToken.get().getToken());
        assertEquals(savedToken.getTokenType(), foundToken.get().getTokenType());
        assertEquals(savedToken.getUser(), foundToken.get().getUser());
    }

    @Test
    public void deleteTokenTest() {
        User user = userRepository.save(TestUtil.generateUser());
        Token savedToken = tokenRepository.save(TestUtil.generateToken(user));
        tokenRepository.delete(savedToken);
        Optional<Token> token = tokenRepository.findById(savedToken.getId());
        assertTrue(token.isEmpty());
    }

    @Test
    public void editTokenTest() {
        User user = userRepository.save(TestUtil.generateUser());
        Token savedToken = tokenRepository.save(TestUtil.generateToken(user));
        savedToken.setRevoked(true);
        Token editedToken = tokenRepository.save(savedToken);
        assertEquals(savedToken, editedToken);
    }

    @Test
    public void findAllTokensByUserTest() {
        User user = userRepository.save(TestUtil.generateUser());

        tokenRepository.saveAll(TestUtil.generateListOfTokens(user));
        List<Token> foundTokens = tokenRepository.findAllTokensByUser(user.getId());

        assertEquals(foundTokens.size(), 3);
    }
}
