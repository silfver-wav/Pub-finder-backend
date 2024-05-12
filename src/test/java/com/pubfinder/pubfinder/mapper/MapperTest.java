package com.pubfinder.pubfinder.mapper;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    PubDto pubDTO = TestUtil.generateMockPubDTO();
    Pub pub = Mapper.INSTANCE.dtoToEntity(pubDTO);
    checkPub(pubDTO, pub);
  }

  @Test
  public void mapPubEntityToDtoTest() {
    Pub pub = TestUtil.generateMockPub();
    PubDto pubDTO = Mapper.INSTANCE.entityToDto(pub);
    checkPub(pubDTO, pub);
  }

  @Test
  public void mapUserEntityToDtoTest() {
    User user = TestUtil.generateMockUser();
    UserDto userDTO = Mapper.INSTANCE.entityToDto(user);
    checkUser(userDTO, user);
  }


  @Test
  public void mapUserDtoToEntityTest() {
    UserDto userDTO = TestUtil.generateMockUserDTO();
    User user = Mapper.INSTANCE.dtoToEntity(userDTO);
    checkUser(userDTO, user);
  }

  private void checkPub(PubDto pubDTO, Pub pub) {
    assertEquals(pubDTO.getName(), pub.getName());
    assertEquals(pubDTO.getLat(), pub.getLat());
    assertEquals(pubDTO.getLng(), pub.getLng());
    assertEquals(pubDTO.getOpeningHours(), pub.getOpeningHours());
    assertEquals(pubDTO.getLocation(), pub.getLocation());
    assertEquals(pubDTO.getDescription(), pub.getDescription());
    assertEquals(pubDTO.getPrice(), pub.getPrice());
    assertEquals(pubDTO.getAdditionalInfoDto(), pub.getAdditionalInfo());
  }

  private void checkUser(UserDto userDTO, User user) {
    assertEquals(userDTO.getFirstname(), user.getFirstname());
    assertEquals(userDTO.getLastname(), user.getLastname());
    assertEquals(userDTO.getEmail(), user.getEmail());
    assertEquals(userDTO.getUsername(), user.getUsername());
    assertEquals(userDTO.getPassword(), user.getPassword());
  }
}
