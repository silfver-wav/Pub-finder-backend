package com.pubfinder.pubfinder.util;

import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.models.Accessibility;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Review;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.Visited;
import com.pubfinder.pubfinder.models.enums.LoudnessRating;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.enums.TokenType;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TestUtil {

  public static Map<DayOfWeek, List<OpeningHours>> generateMockOpeningHours() {
    Map<DayOfWeek, List<OpeningHours>> openingHours = new HashMap<>();

    LocalTime startTime = LocalTime.of(9, 0);
    LocalTime endTime = LocalTime.of(18, 0);

    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      List<OpeningHours> hours = List.of(new OpeningHours(startTime, endTime));
      openingHours.put(dayOfWeek, hours);
    }

    return openingHours;
  }

  public static Pub generateMockPub() {
    return Pub.builder()
        .id(UUID.randomUUID())
        .name("name")
        .lat(1.0)
        .lng(1.0)
        .openingHours(generateMockOpeningHours())
        .location("location")
        .description("description")
        .price("$")
        .website("google.com")
        .outDoorSeating(true)
        .washroom(true)
        .accessibility(generateMockAccessibility())
        .build();
  }

  public static PubDto generateMockPubDTO() {
    return PubDto.builder()
        .name("name")
        .lat(1.0)
        .lng(1.0)
        .openingHours(generateMockOpeningHours())
        .location("location")
        .description("description")
        .price("$")
        .website("google.com")
        .outDoorSeating(true)
        .washroom(true)
        .accessibility(generateMockAccessibility())
        .build();
  }

  public static List<Pub> generateListOfMockPubs() {
    Pub pub1 = generateMockPub();
    pub1.setId(UUID.randomUUID());
    pub1.setLat(40.7128);
    pub1.setLng(74.0060);

    Pub pub2 = generateMockPub();
    pub2.setId(UUID.randomUUID());
    pub2.setLat(40.7130);
    pub2.setLng(74.0064);

    Pub pub3 = generateMockPub();
    pub3.setId(UUID.randomUUID());
    pub3.setLat(40.7132);
    pub3.setLng(74.0061);

    return List.of(pub1, pub2, pub3);
  }

  public static User generateMockUser() {
    return User.builder()
        .id(UUID.randomUUID())
        .firstname("firstName")
        .lastname("lastName")
        .email("email")
        .username("username")
        .password("password")
        .role(Role.USER)
        .build();
  }

  public static UserDto generateMockUserDTO() {
    return UserDto.builder()
        .id(UUID.randomUUID())
        .firstname("firstName")
        .lastname("lastName")
        .email("email")
        .username("username")
        .password("password")
        .build();
  }

  public static Token generateMockToken(User user) {
    return Token.builder()
        .token("token")
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .user(user)
        .build();
  }

  public static List<Token> generateListOfMockedTokens(User user) {
    Token token1 = generateMockToken(user);
    token1.setToken("token1");

    Token token2 = generateMockToken(user);
    token2.setToken("token2");

    Token token3 = generateMockToken(user);
    token3.setToken("token3");

    return List.of(token1, token2, token3);
  }

  public static AuthenticationResponse generateMockAuthenticationResponse() {
    return AuthenticationResponse.builder()
        .accessToken(
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcxMTU2ODQ1MiwiZXhwIjoxNzExNTczODUyfQ.zcLczdIhND1hnJMQvT4OMRzqTdBTw76O6Wb70uSzRks")
        .refreshToken(
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcxMTU2ODQ1MiwiZXhwIjoxNzExNjU0ODUyfQ.SSezTPT73RI03rM109o2Geetr1jHzOZgZoJR4qTLa-U")
        .build();
  }

  public static Accessibility generateMockAccessibility() {
    return Accessibility.builder()
        .accessibleEntrance(true)
        .accessibleSeating(true)
        .accessibleParking(false)
        .build();
  }

  public static Visited generateUserVisitedPub() {
    return Visited.builder()
        .visitor(generateMockUser())
        .pub(generateMockPub())
        .visitedDate(LocalDateTime.now())
        .build();
  }

  public static Review generateMockReview(User user, Pub pub) {
    return Review.builder()
        .pub(pub)
        .reviewer(user)
        .reviewDate(LocalDateTime.now())
        .rating(5)
        .build();
  }

  public static ReviewDto generateMockReviewDTO() {
    return ReviewDto.builder()
        .id(UUID.randomUUID())
        .pubId(UUID.randomUUID())
        .username("username")
        .reviewDate(LocalDateTime.now())
        .rating(5)
        .build();
  }

  public static Visited generateMockVisited() {
    return Visited.builder()
        .visitedDate(LocalDateTime.now())
        .pub(TestUtil.generateMockPub())
        .visitor(TestUtil.generateMockUser())
        .build();
  }

  public static List<Review> generateListOfMockReviews() {
    Random rand = new Random();
    Pub pub = generateMockPub();
    List<Review> reviews = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Review review = generateMockReview(generateMockUser(), pub);
      review.setRating(rand.nextInt(5));
      review.setService(rand.nextInt(5));
      review.setToilets(rand.nextInt(5));
      if (rand.nextBoolean()) {
        review.setLoudness(LoudnessRating.values()[rand.nextInt(5)]);
      }
      reviews.add(review);
    }
    return reviews;
  }
}
