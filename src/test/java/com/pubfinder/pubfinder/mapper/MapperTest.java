package com.pubfinder.pubfinder.mapper;


import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.Pub;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
        PubDTO pubDTO = new PubDTO(null,"name",1.0,1.0,"3pm-3am","location","description");

        Pub pub = Mapper.INSTANCE.dtoToEntity(pubDTO);
        check(pubDTO, pub);
    }

    @Test
    public void mapEntityToDto() {
        Pub pub = new Pub("name",1.0,1.0,"3pm-3am","location","description");

        PubDTO pubDTO = Mapper.INSTANCE.entityToDto(pub);
        check(pubDTO, pub);
    }

    private void check(PubDTO pubDTO, Pub pub) {
        assertEquals(pubDTO.getName(), pub.getName());
        assertEquals(pubDTO.getLat(), pub.getLat());
        assertEquals(pubDTO.getLng(), pub.getLng());
        assertEquals(pubDTO.getOpen(), pub.getOpen());
        assertEquals(pubDTO.getLocation(), pub.getLocation());
        assertEquals(pubDTO.getDescription(), pub.getDescription());
    }
}
