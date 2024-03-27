package com.pubfinder.pubfinder.db;

import com.pubfinder.pubfinder.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository  extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String token);
}
