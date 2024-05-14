package com.pubfinder.pubfinder.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pubfinder.pubfinder.db.VisitedRepository;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.models.Pub.Pub;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.Visited;
import com.pubfinder.pubfinder.util.TestUtil;
import java.util.Optional;
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
public class VisitedServiceTest {

  @Autowired
  private VisitedService visitedService;

  @MockBean
  private VisitedRepository visitedRepository;

  @MockBean
  private UserService userService;

  @MockBean
  private PubService pubService;

  @Test
  public void saveVisitTest() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenReturn(user);
    when(pubService.getPub(any())).thenReturn(pub);
    Visited visited = TestUtil.generateMockVisited();

    when(visitedRepository.findByPubAndVisitor(pub, user)).thenReturn(Optional.of(visited));

    visitedService.saveVisit(pub.getId(), user.getUsername());

    verify(visitedRepository, times(1)).findByPubAndVisitor(pub, user);
    verify(visitedRepository, times(1)).save(any(Visited.class));
  }

  @Test
  public void saveReviewTest_UserNotFound() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenThrow(ResourceNotFoundException.class);
    assertThrows(ResourceNotFoundException.class,
        () -> visitedService.saveVisit(pub.getId(), user.getUsername()));
  }

  @Test
  public void saveReviewTest_PubNotFound() throws ResourceNotFoundException {
    User user = TestUtil.generateMockUser();
    Pub pub = TestUtil.generateMockPub();
    when(userService.getUser(any())).thenReturn(user);
    when(pubService.getPub(any())).thenThrow(ResourceNotFoundException.class);
    assertThrows(ResourceNotFoundException.class,
        () -> visitedService.saveVisit(pub.getId(), user.getUsername()));
  }

  @Test
  public void deleteTest() throws ResourceNotFoundException {
    Visited visited = TestUtil.generateMockVisited();
    when(visitedRepository.findById(any())).thenReturn(Optional.of(visited));
    visitedService.deleteVisit(visited.getId());
    verify(visitedRepository, times(1)).delete(visited);
  }

  @Test
  public void deleteTest_NotFound() {
    when(visitedRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> visitedService.deleteVisit(any()));
  }

}
