package com.pubfinder.pubfinder.util;

import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.Pub;

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
}
