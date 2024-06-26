package com.pubfinder.pubfinder.security;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.User.User;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.service.PubService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "spring.datasource.url=",
    "spring.jpa.database-platform=",
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.cache.type=none",
    "bucket4j.enabled=false"
})
@AutoConfigureMockMvc()
public class PubsSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PubService pubService;

  @Autowired
  private AuthenticationService authenticationService;

  @MockBean
  private UserDetailsService userDetailsService;

  @BeforeEach
  public void setup() {
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void createPubAdminAuthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.ADMIN);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(pubService.save(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

    mockMvc.perform(post("/pub/createPub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isCreated());
  }

  @Test
  public void createPubUserUnauthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.USER);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(pubService.save(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

    mockMvc.perform(post("/pub/createPub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isForbidden());
  }

  @Test
  public void createPubUnauthenticatedTest() throws Exception {
    mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void editPubAdminAuthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.ADMIN);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(pubService.edit(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

    mockMvc.perform(put("/pub/editPub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isOk());
  }

  @Test
  public void editPubUserUnauthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.USER);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(pubService.edit(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

    mockMvc.perform(put("/pub/editPub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isForbidden());
  }

  @Test
  public void editPubUnauthenticatedTest() throws Exception {
    mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void deletePubAdminAuthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.ADMIN);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);
    mockMvc.perform(delete("/pub/deletePub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isNoContent());
  }

  @Test
  public void deletePubUserUnauthorizedTest() throws Exception {
    User user = TestUtil.generateMockUser();
    user.setRole(Role.USER);
    String jwtToken = authenticationService.generateToken(user);

    when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(user);

    mockMvc.perform(delete("/pub/deletePub")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pub))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
        .andExpect(status().isForbidden());
  }

  @Test
  public void deletePubUnauthenticatedTest() throws Exception {
    mockMvc.perform(delete("/pub/deletePub").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  PubDto pub = TestUtil.generateMockPubDTO();
}