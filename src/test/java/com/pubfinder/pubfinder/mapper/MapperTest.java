package com.pubfinder.pubfinder.mapper;


import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.models.Pub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MapperTest {


    @Test
    public void mapDtoToEntity() {
        PubDTO pubDTO = new PubDTO(null,"name",1.0,1.0,"3pm-3am","location","description");

        Pub pub = PubMapper.INSTANCE.dtoToEntity(pubDTO);
        check(pubDTO, pub);
    }

    @Test
    public void mapEntityToDto() {
        Pub pub = new Pub("name",1.0,1.0,"3pm-3am","location","description");

        PubDTO pubDTO = PubMapper.INSTANCE.entityToDto(pub);
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
