package com.pubfinder.pubfinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.dto.AdditionalInfoDto;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
    "spring.cache.type=none",
    "bucket4j.enabled=false",
    "spring.datasource.url=",
    "spring.jpa.database-platform=",
    "spring.jpa.hibernate.ddl-auto=none"
})
public class PubServiceTest {

  @Autowired
  PubService pubService;

  @MockBean
  UserService userService;

  @MockBean
  private PubRepository pubRepository;

  @Test
  public void getPubTest() throws ResourceNotFoundException {
    UUID pubId = pub.getId();
    when(pubRepository.findById(any())).thenReturn(Optional.of(pub));
    Pub result = pubService.getPub(pubId);
    assertEquals(pub, result);
    verify(pubRepository, times(1)).findById(pubId);
  }

  @Test
  public void getPubTest_NOT_FOUND() {
    UUID pubId = pub.getId();
    when(pubRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> pubService.getPub(pubId));
    verify(pubRepository, times(1)).findById(pubId);
  }

  @Test
  public void searchPubsByTermTest() {
    PubDto bigBen = PubDto.builder().id(UUID.randomUUID()).name("The Big Ben Pub").build();
    PubDto liffey = PubDto.builder().id(UUID.randomUUID()).name("The Liffey").build();
    List<Object[]> dbRs = List.of(new Object[]{bigBen.getId(), bigBen.getName()},
        new Object[]{liffey.getId(), liffey.getName()});

    when(pubRepository.findPubsByNameContaining(any())).thenReturn(dbRs);
    List<PubDto> result = pubService.searchPubsByTerm("The");
    assertEquals(List.of(bigBen, liffey), result);
    verify(pubRepository, times(1)).findPubsByNameContaining("The");
  }

  @Test
  public void getPubsTest() {
    List<Pub> pubs = new ArrayList<>(TestUtil.generateListOfMockPubs());
    when(pubRepository.findPubsWithInRadius(40.712810, 74.006010, 1)).thenReturn(pubs);
    List<PubDto> result = pubService.getPubs(40.712810, 74.006010, 1.0);
    assertEquals(3, result.size());
    verify(pubRepository, times(1)).findPubsWithInRadius(40.712810, 74.006010, 1);
  }

  @Test
  public void savePubTest() throws BadRequestException {
    when(pubRepository.save(any())).thenReturn(pub);
    PubDto result = pubService.save(pub);
    assertEquals(Mapper.INSTANCE.entityToDto(pub), result);
    verify(pubRepository, times(1)).save(pub);
  }

  @Test
  public void savePubTest_BAD_REQUEST() {
    assertThrows(BadRequestException.class, () -> pubService.save(null));
  }

  @Test
  public void editPubTest() throws BadRequestException, ResourceNotFoundException {
    Pub updatedPub = Pub.builder()
        .id(pub.getId())
        .name("something else")
        .lat(pub.getLat())
        .lng(pub.getLng())
        .openingHours(pub.getOpeningHours())
        .location(pub.getLocation())
        .description(pub.getDescription())
        .price(pub.getPrice())
        .build();

    when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));
    when(pubRepository.save(any())).thenReturn(updatedPub);
    PubDto result = pubService.edit(updatedPub);
    assertEquals(Mapper.INSTANCE.entityToDto(updatedPub), result);
    verify(pubRepository, times(1)).save(updatedPub);
  }

  @Test
  public void editPubTest_BAD_REQUEST() {
    Pub updatedPub = Pub.builder()
        .id(null)
        .name("something else")
        .lat(pub.getLat())
        .lng(pub.getLng())
        .openingHours(pub.getOpeningHours())
        .location(pub.getLocation())
        .description(pub.getDescription())
        .price(pub.getPrice())
        .build();
    when(pubRepository.findById(pub.getId())).thenReturn(Optional.empty());
    assertThrows(BadRequestException.class, () -> pubService.edit(updatedPub));
  }

  @Test
  public void deletePubTest() throws BadRequestException {
    doNothing().when(pubRepository).delete(pub);
    pubService.delete(pub);
    verify(pubRepository, times(1)).delete(any());
  }

  @Test
  public void deletePubTest_BAD_REQUEST() {
    assertThrows(BadRequestException.class, () -> pubService.delete(null));
  }

  @Test
  public void updateRatingsInPubTest() throws BadRequestException, ResourceNotFoundException {
    when(pubRepository.findAllReviewsForPub(any())).thenReturn(
        TestUtil.generateListOfMockReviews());
    when(pubRepository.findById(pub.getId())).thenReturn(Optional.of(pub));

    when(pubRepository.save(any())).thenReturn(pub);

    PubDto result = pubService.updateRatingsInPub(pub);
    assertNotEquals(0, result.getRating());
    verify(pubRepository, times(1)).save(any(Pub.class));
  }

  @Test
  public void getAdditionalInfo() throws ResourceNotFoundException {
    AdditionalInfo info = TestUtil.generateMockAdditionalInfo();

    when(pubRepository.findAdditionalInfoForPub(any())).thenReturn(
        Optional.ofNullable(info));

    AdditionalInfoDto foundInfo = pubService.getAdditionalInfo(UUID.randomUUID());

    assert info != null;
    assertEquals(foundInfo.getWebsite(), info.getWebsite());
    assertEquals(foundInfo.getAccessibility(), info.getAccessibility());
    assertEquals(foundInfo.getWashroom(), info.getWashroom());
    assertEquals(foundInfo.getOutDoorSeating(), info.getOutDoorSeating());
  }

  @Test
  public void getAdditionalInfo_NOT_FOUND() {
    UUID id = UUID.randomUUID();
    when(pubRepository.findAdditionalInfoForPub(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> pubService.getAdditionalInfo(id));
    verify(pubRepository, times(1)).findAdditionalInfoForPub(id);
  }

  private final User user = TestUtil.generateMockUser();
  private final Pub pub = TestUtil.generateMockPub();
}
