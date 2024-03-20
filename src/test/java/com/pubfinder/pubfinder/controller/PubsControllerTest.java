package com.pubfinder.pubfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.PubsService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getPubsTest() throws Exception {
        List<PubDTO> pubs = new ArrayList<>(List.of(pub));
        when(pubsService.getPubs(1.0,1.0,1.0)).thenReturn(pubs);

        mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pubs)));
    }

    @Test
    public void getPubByNameTest() throws Exception {
        when(pubsService.getPubByName("name")).thenReturn(ResponseEntity.ok().body(pub));

        mockMvc.perform(get("/pub/getPub/{name}", "name")).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pub)));
    }

    @Test
    public void savePubTest() throws Exception {
        when(pubsService.savePub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(pub));

        mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void savePubTest_BAD_REQUEST() throws Exception {
        when(pubsService.savePub(null)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void editPubTest() throws Exception {
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.ok().body(pub));

        mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isOk());
    }

    @Test
    public void editPubTest_BAD_REQUEST() throws Exception {
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isBadRequest());
    }

    @Test
    public void editPubTest_NOT_FOUND() throws Exception {
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isNotFound());
    }

    @Test
    public void deletePubTest() throws Exception {
        when(pubsService.deletePub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/pub/deletePub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isOk());
    }

    PubDTO pub = new PubDTO(UUID.randomUUID(), "name", 1.0, 1.0, TestUtil.generateMockOpeningHours(), "location", "desc");
}
