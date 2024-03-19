package com.pubfinder.pubfinder.mapper;


import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.OpeningHours;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "spring.cache.type=none",
        "bucket4j.enabled=false",
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
public class MapperTest {
    @Test
    public void mapDtoToEntity() {
        PubDTO pubDTO = TestUtil.generateMockPubDTO();
        Pub pub = Mapper.INSTANCE.dtoToEntity(pubDTO);
        check(pubDTO, pub);
    }

    @Test
    public void mapEntityToDto() {
        Pub pub = TestUtil.generateMockPub();
        PubDTO pubDTO = Mapper.INSTANCE.entityToDto(pub);
        check(pubDTO, pub);
    }

    private void check(PubDTO pubDTO, Pub pub) {
        assertEquals(pubDTO.getName(), pub.getName());
        assertEquals(pubDTO.getLat(), pub.getLat());
        assertEquals(pubDTO.getLng(), pub.getLng());
        assertEquals(pubDTO.getOpeningHours(), pub.getOpeningHours());
        assertEquals(pubDTO.getLocation(), pub.getLocation());
        assertEquals(pubDTO.getDescription(), pub.getDescription());
    }
}
