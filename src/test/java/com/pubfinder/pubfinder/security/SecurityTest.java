package com.pubfinder.pubfinder.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.models.enums.Role;
import com.pubfinder.pubfinder.service.PubsService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.apache.coyote.BadRequestException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
@AutoConfigureMockMvc()
public class SecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PubsService pubsService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void rateLimitTest() throws Exception {
        List<PubDTO> pubs = new ArrayList<>(List.of(pub));
        when(pubsService.getPubs(1.0,1.0,1.0)).thenReturn(pubs);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pubs)));
        }

        mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isTooManyRequests());
    }

    @Test
    public void createPubAuthorizedTest() throws Exception {
        mockAuthentication();
        when(pubsService.savePub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

        mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    public void createPubUnauthorizedTest() throws Exception {
        mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // @Role() TODO check role
    @Test
    public void editPubAuthorizedTest() throws Exception {
        mockAuthentication();
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

        mockMvc.perform(post("/pub/editPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void editPubUnauthorizedTest() throws Exception {
        mockMvc.perform(post("/pub/editPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deletePubAuthorizedTest() throws Exception {
        mockAuthentication();
        mockMvc.perform(post("/pub/deletePub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePubUnauthorizedTest() throws Exception {
        mockMvc.perform(post("/pub/deletePub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private void mockAuthentication() {

        // gör en token
        User user = TestUtil.generateMockUser();
        user.setRole(Role.ADMIN);
        when(authenticationService.extractUsername(any())).thenReturn(user.getUsername());
        when(userDetailsService.loadUserByUsername(any())).thenReturn(user);
        when(authenticationService.isTokenValid(any(), any())).thenReturn(true);
    }

    PubDTO pub = new PubDTO(UUID.randomUUID(), "name", 1.0, 1.0, TestUtil.generateMockOpeningHours(), "location", "desc");
}