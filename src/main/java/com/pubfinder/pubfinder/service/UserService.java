package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.TokenRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.enums.TokenType;
import com.pubfinder.pubfinder.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse registerUser(User user) throws BadRequestException {
        if (userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        String jwtToken = authenticationService.generateToken(savedUser);
        String refresherToken = authenticationService.generateRefresherToken(savedUser);
        saveToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refresherToken)
                .build();
    }

    public void deleteUser(User user, HttpServletRequest request) throws ResourceNotFoundException {
        isRequestAllowed(user, request);
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("User with id: " + user.getId() + " was not found");
        }
        deleteAllUserTokens(foundUser.get());
        userRepository.delete(foundUser.get());
    }

    public UserDTO editUser(User user, HttpServletRequest request) throws BadRequestException, ResourceNotFoundException {
        if (user == null) {
            throw new BadRequestException();
        }

        Optional<User> foundUser = userRepository.findById(user.getId());

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("User with the id: " + user.getId() + " was not found");
        }

        isRequestAllowed(foundUser.get(), request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        deleteAllUserTokens(foundUser.get());
        User editedUser = userRepository.save(user);
        return Mapper.INSTANCE.entityToDto(editedUser);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws ResourceNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User with email: " + loginRequest.getEmail() + " not found"));
        var accessToken = authenticationService.generateToken(user);
        var refreshToken = authenticationService.generateRefresherToken(user);
        deleteAllUserTokens(user);
        saveToken(user, accessToken);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) throws BadRequestException, ResourceNotFoundException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            throw new BadRequestException();
        }
        String refreshToken = authHeader.substring(7);
        String finalRefreshToken = refreshToken;
        String userEmail = Optional.ofNullable(authenticationService.extractUsername(refreshToken)).orElseThrow(() -> new ResourceNotFoundException("User with refresherToken: " + finalRefreshToken + " was not found"));

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User with the email: " + userEmail + " was not found"));
        if (authenticationService.isTokenValid(refreshToken, user)) {
            String accessToken = authenticationService.generateToken(user);
            refreshToken = authenticationService.generateRefresherToken(user);
            deleteAllUserTokens(user);
            saveToken(user, accessToken);
            return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        }
        throw new BadCredentialsException("Token was invalid");
    }

    public void revokeUserAccess(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        deleteAllUserTokens(user);
    }

    private void deleteAllUserTokens(User user) {
        List<Token> tokens = tokenRepository.findAllTokensByUser(user.getId());
        tokens.forEach((token -> tokenRepository.delete(token)));
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

    private void isRequestAllowed(User user, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = authenticationService.extractUsername(jwt);
        if (!username.equals(user.getUsername())) {
            Optional<User> userDetails = userRepository.findByUsername(username);
            if (userDetails.isEmpty() || !userDetails.get().getRole().equals(Role.ADMIN)) {
                throw new BadCredentialsException("Only admin or the user itself can delete a user");
            }
        }
    }
}