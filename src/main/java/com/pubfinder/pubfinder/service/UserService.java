package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.ReviewRepository;
import com.pubfinder.pubfinder.db.TokenRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.db.VisitedRepository;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.dto.VisitedDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.User.Token;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.Visited;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.enums.TokenType;
import com.pubfinder.pubfinder.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
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

  @Autowired
  private VisitedRepository visitedRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  /**
   * Register user authentication response.
   *
   * @param user the user
   * @return the access- and refresher-token
   * @throws BadRequestException user with the same email or/and username already exists
   */
  public AuthenticationResponse registerUser(User user) throws BadRequestException {
    if (userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(
        user.getUsername()).isPresent()) {
      throw new BadRequestException();
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    User savedUser = userRepository.save(user);
    String jwtToken = authenticationService.generateToken(savedUser);
    String refresherToken = authenticationService.generateRefresherToken(savedUser);
    saveToken(savedUser, jwtToken);
    return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refresherToken)
        .build();
  }

  /**
   * Delete user, tokens, reviews and visits.
   *
   * @param user    the user
   * @param request the request
   * @throws ResourceNotFoundException the resource not found exception
   */
  public void delete(User user, HttpServletRequest request) throws ResourceNotFoundException {
    isRequestAllowed(user, request);
    User foundUser = userRepository.findById(user.getId()).orElseThrow(
        () -> new ResourceNotFoundException("User with id: " + user.getId() + " was not found"));

    deleteAllUserTokens(foundUser);
    deleteAllUserVisits(foundUser);
    deleteAllUserReviews(foundUser);
    userRepository.delete(foundUser);
  }

  /**
   * Edit user. Only admin or the user themselves can edit the object
   *
   * @param user    the user
   * @param request the request
   * @return the user dto
   * @throws BadRequestException       the user param is empty exception
   * @throws ResourceNotFoundException the user not found exception
   */
  public UserDto edit(User user, HttpServletRequest request)
      throws BadRequestException, ResourceNotFoundException {
    if (user == null) {
      throw new BadRequestException();
    }

    User foundUser = userRepository.findById(user.getId()).orElseThrow(
        () -> new ResourceNotFoundException(
            "User with the id: " + user.getId() + " was not found"));

    isRequestAllowed(foundUser, request);

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    deleteAllUserTokens(foundUser);
    User editedUser = userRepository.save(user);
    return Mapper.INSTANCE.entityToDto(editedUser);
  }

  /**
   * Login user, and return the access- and refresher-token.
   *
   * @param loginRequest the login request
   * @return the authentication response
   * @throws ResourceNotFoundException the user not found exception
   */
  public AuthenticationResponse login(LoginRequest loginRequest) throws ResourceNotFoundException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));

    var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
        () -> new ResourceNotFoundException(
            "User with username: " + loginRequest.getUsername() + " not found"));

    var accessToken = authenticationService.generateToken(user);
    var refreshToken = authenticationService.generateRefresherToken(user);

    deleteAllUserTokens(user);
    saveToken(user, accessToken);

    return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
        .build();
  }

  /**
   * Refresh users access token.
   *
   * @param request the request
   * @return the authentication response
   * @throws BadRequestException       the authHeader was empty exception
   * @throws ResourceNotFoundException the user or refreshToken not found exception
   */
  public AuthenticationResponse refreshToken(HttpServletRequest request)
      throws BadRequestException, ResourceNotFoundException {
    String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (refreshToken == null) {
      throw new BadRequestException();
    }

    String finalRefreshToken = refreshToken;
    String username = Optional.ofNullable(authenticationService.extractUsername(refreshToken))
        .orElseThrow(() -> new ResourceNotFoundException(
            "User with refresherToken: " + finalRefreshToken + " was not found"));

    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new ResourceNotFoundException(
            "User with the email: " + username + " was not found"));

    if (authenticationService.isTokenValid(refreshToken, user)) {
      String accessToken = authenticationService.generateToken(user);
      refreshToken = authenticationService.generateRefresherToken(user);
      deleteAllUserTokens(user);
      saveToken(user, accessToken);
      return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
          .build();
    }

    throw new BadCredentialsException("Token was invalid");
  }

  /**
   * Revoke user access.
   *
   * @param email the email
   */
  public void revokeUserAccess(String email) {
    User user = userRepository.findByEmail(email).orElseThrow();
    deleteAllUserTokens(user);
  }

  /**
   * Gets visited pubs.
   *
   * @param username the users username
   * @return the visited pubs
   */
  public List<VisitedDto> getVisitedPubs(String username) {
    List<Visited> visits = userRepository.getVisitedPubs(username);
    List<VisitedDto> visitsDto = new ArrayList<>();
    for (Visited visit : visits) {
      visitsDto.add(VisitedDto.builder().pubDto(Mapper.INSTANCE.entityToDto(visit.getPub()))
          .visitedDate(visit.getVisitedDate()).id(visit.getId()).build());
    }
    return visitsDto;
  }

  /**
   * Gets user.
   *
   * @param username the users username
   * @return the user
   * @throws ResourceNotFoundException the user not found exception
   */
  public User getUser(String username) throws ResourceNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("User with username: " + username + " not found");
    }
    return user.get();
  }

  /**
   * Gets user reviews.
   *
   * @param username the users username
   * @return the user reviews
   */
  public List<ReviewDto> getUserReviews(String username) {
    return userRepository.findAllReviewsByUser(username).stream().map(Mapper.INSTANCE::entityToDto)
        .toList();
  }

  private void deleteAllUserTokens(User user) {
    List<Token> tokens = tokenRepository.findAllTokensByUser(user.getId());
    tokens.forEach((token -> tokenRepository.delete(token)));
  }

  private void deleteAllUserVisits(User user) {
    visitedRepository.deleteAllByVisitor(user);
  }

  private void deleteAllUserReviews(User user) {
    reviewRepository.deleteAllByReviewer(user);
  }

  private void saveToken(User user, String accessToken) {
    Token token = Token.builder().token(accessToken).tokenType(TokenType.BEARER).revoked(false)
        .expired(false).user(user).build();
    tokenRepository.save(token);
  }

  private void isRequestAllowed(User user, HttpServletRequest request) {
    String jwt = request.getHeader("Authorization").substring(7);
    String username = authenticationService.extractUsername(jwt);
    if (!username.equals(user.getUsername())) {
      Optional<User> userDetails = userRepository.findByUsername(username);
      if (userDetails.isEmpty() || !userDetails.get().getRole().equals(Role.ADMIN)) {
        throw new BadCredentialsException("Only admin or the user themselves can delete a user");
      }
    }
  }
}