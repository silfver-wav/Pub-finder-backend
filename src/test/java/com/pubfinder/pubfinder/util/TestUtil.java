package com.pubfinder.pubfinder.util;

import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.models.enums.TokenType;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                .build();
    }

    public static PubDTO generateMockPubDTO() {
        return PubDTO.builder()
                .name("name")
                .lat(1.0)
                .lng(1.0)
                .openingHours(generateMockOpeningHours())
                .location("location")
                .description("description")
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

    public static User generateUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .username("username")
                .password("password")
                .role(Role.USER)
                .build();
    }


    public static Token generateToken(User user) {
        return Token.builder()
                .token("token")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
    }

    public static List<Token> generateListOfTokens(User user) {
        Token token1 = generateToken(user);
        token1.setToken("token1");

        Token token2 = generateToken(user);
        token2.setToken("token2");

        Token token3 = generateToken(user);
        token3.setToken("token3");

        return List.of(token1, token2, token3);
    }

    public static AuthenticationResponse generateAuthenticationResponse() {
        return AuthenticationResponse.builder()
                .accessToken("token")
                .refreshToken("refresher")
                .build();
    }
}
