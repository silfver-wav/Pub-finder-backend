package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.TokenRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.enums.TokenType;
import com.pubfinder.pubfinder.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(User user) throws BadRequestException {
        if (userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(user.getUsername()).isPresent())
            throw new BadRequestException();

        User savedUser = userRepository.save(user);
        var jwtToken = authenticationService.generateToken(savedUser);
        var refresherToken = authenticationService.generateRefresherToken(savedUser);
        saveToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refresherToken)
                .build();
    }

    public ResponseEntity<String> deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        userRepository.delete(user.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserDTO> editUser(User user) {
        if (user.getId() == null) return ResponseEntity.badRequest().build();

        Optional<User> foundUser = Optional.of(userRepository.findById(user.getId())).orElseThrow(); // Throw ResourceNotFound("User with id not found);

        if (!foundUser.get().getUsername().equals(user.getUsername()) || !foundUser.get().getEmail().equals(user.getEmail())) // Cannot change username or password
            return ResponseEntity.badRequest().build(); // Throw BadRequest

        User editedUser = userRepository.save(user);
        return ResponseEntity.ok().body(Mapper.INSTANCE.entityToDto(editedUser));
    }

    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(); // TODO: change this so that custom authentication error is thrown
        var accessToken = authenticationService.generateToken(user);
        var refreshToken = authenticationService.generateRefresherToken(user);
        // revokeAllUserTokens(user);
        saveToken(user, accessToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticationResponse(accessToken, refreshToken));
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            throw new Exception();
        }
        String refreshToken = authHeader.substring(7);
        String userEmail = Optional.ofNullable(authenticationService.extractUsername(refreshToken)).orElseThrow(); // TODO: change this so that custom authentication error is thrown

        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (authenticationService.isTokenValid(refreshToken, user)) {
            String accessToken = authenticationService.generateToken(user);
            refreshToken = authenticationService.generateRefresherToken(user);
            // revokeAllUserTokens(user);
            saveToken(user, accessToken);
            return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        }
        throw new Exception();
    }

    public void revokeUserAccess(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        revokeAllUserTokens(user);
    }

    private void revokeAllUserTokens(User user) {
        // TODO implement
    }

    private void saveToken(User user, String accessToken) {
        Token token = Token.builder()
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }
}