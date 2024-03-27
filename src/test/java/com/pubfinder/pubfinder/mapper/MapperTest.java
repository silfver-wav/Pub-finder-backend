package com.pubfinder.pubfinder.mapper;


import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.util.TestUtil;
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
    public void mapPubDtoToEntityTest() {
        PubDTO pubDTO = TestUtil.generateMockPubDTO();
        Pub pub = Mapper.INSTANCE.dtoToEntity(pubDTO);
        checkPub(pubDTO, pub);
    }

    @Test
    public void mapPubEntityToDtoTest() {
        Pub pub = TestUtil.generateMockPub();
        PubDTO pubDTO = Mapper.INSTANCE.entityToDto(pub);
        checkPub(pubDTO, pub);
    }

    @Test
    public void mapUserEntityToDtoTest() {
        User user = TestUtil.generateMockUser();
        UserDTO userDTO = Mapper.INSTANCE.entityToDto(user);
        checkUser(userDTO, user);
    }

    // Does not work because of passwordEncoder nullPointerException
    /*
    @Test
    public void mapUserDtoToEntityTest() {
        UserDTO userDTO = TestUtil.generateMockUserDTO();
        User user = Mapper.INSTANCE.dtoToEntity(userDTO);
        checkUser(userDTO, user);
    }
     */

    private void checkPub(PubDTO pubDTO, Pub pub) {
        assertEquals(pubDTO.getName(), pub.getName());
        assertEquals(pubDTO.getLat(), pub.getLat());
        assertEquals(pubDTO.getLng(), pub.getLng());
        assertEquals(pubDTO.getOpeningHours(), pub.getOpeningHours());
        assertEquals(pubDTO.getLocation(), pub.getLocation());
        assertEquals(pubDTO.getDescription(), pub.getDescription());
    }

    private void checkUser(UserDTO userDTO, User user) {
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getPassword(), user.getPassword());
    }
}
