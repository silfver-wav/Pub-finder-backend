package com.pubfinder.pubfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.PubMapper;
import com.pubfinder.pubfinder.security.SecurityConfig;
import com.pubfinder.pubfinder.service.PubsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = PubsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PubsControllerTest {

    @MockBean
    private PubsService pubsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getPubsTest() throws Exception {
        List<PubDTO> pubs = new ArrayList<>(List.of(pub));
        when(pubsService.getPubs(1.0,1.0,1.0)).thenReturn(ResponseEntity.ok().body(pubs));

        mockMvc.perform(get("/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isOk()).andExpect(content().json(new ObjectMapper().writeValueAsString(pubs)));
    }

    @Test
    public void getPubByNameTest() throws Exception {
        when(pubsService.getPubByName("name")).thenReturn(ResponseEntity.ok().body(pub));

        mockMvc.perform(get("/getPub/{name}", "name")).andExpect(status().isOk()).andExpect(content().json(new ObjectMapper().writeValueAsString(pub)));
    }

    @Test
    public void savePubTest() throws Exception {
        when(pubsService.savePub(PubMapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(pub));

        mockMvc.perform(post("/savePub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void savePubTest_BAD_REQUEST() throws Exception {
        when(pubsService.savePub(null)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post("/savePub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void editPubTest() throws Exception {
        when(pubsService.editPub(PubMapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.ok().body(pub));

        mockMvc.perform(put("/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isOk());
    }

    @Test
    public void editPubTest_BAD_REQUEST() throws Exception {
        when(pubsService.editPub(PubMapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isBadRequest());
    }

    @Test
    public void editPubTest_NOT_FOUND() throws Exception {
        when(pubsService.editPub(PubMapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isNotFound());
    }

    @Test
    public void deletePubTest() throws Exception {
        when(pubsService.deletePub(PubMapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/deletePub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isOk());
    }

    PubDTO pub = new PubDTO(UUID.randomUUID(), "name", 1.0, 1.0,"open", "location", "desc");
}
