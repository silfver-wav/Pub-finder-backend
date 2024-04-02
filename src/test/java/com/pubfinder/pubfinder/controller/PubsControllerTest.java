package com.pubfinder.pubfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pubfinder.pubfinder.dto.PubDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.PubsService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
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
        List<PubDTO> pubs = new ArrayList<>(List.of(
                PubDTO.builder()
                        .name("Söderkällaren")
                        .build()

        ));
        when(pubsService.getPubs(1.0,1.0,1.0)).thenReturn(pubs);

        mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) // Ensure UTF-8 charset
                .andExpect(content().json(objectMapper.writeValueAsString(pubs)));
    }

    @Test
    public void getPubByIdTest() throws Exception {
        UUID id = pub.getId();
        when(pubsService.getPub(id)).thenReturn(pub);

        mockMvc.perform(get("/pub/getPub/{id}", id)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pub)));
    }


    @Test
    public void searchForPubsTest() throws Exception {
        List<PubDTO> pubs = new ArrayList<>(List.of(pub));
        when(pubsService.searchPubsByTerm("name")).thenReturn(pubs);
        mockMvc.perform(get("/pub/searchPubs/{term}", "name")).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pubs)));
    }

    @Test
    public void savePubTest() throws Exception {
        when(pubsService.savePub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

        mockMvc.perform(post("/pub/createPub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void savePubTest_BAD_REQUEST() throws Exception {
        when(pubsService.savePub(null)).thenThrow(BadRequestException.class);

        mockMvc.perform(post("/pub/createPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void editPubTest() throws Exception {
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenReturn(pub);

        mockMvc.perform(put("/pub/editPub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isOk());
    }

    @Test
    public void editPubTest_BAD_REQUEST() throws Exception {
        when(pubsService.editPub(null)).thenThrow(BadRequestException.class);

        mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void editPubTest_NOT_FOUND() throws Exception {
        when(pubsService.editPub(Mapper.INSTANCE.dtoToEntity(pub))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/pub/editPub").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pub))).andExpect(status().isNotFound());
    }

    @Test
    public void deletePubTest() throws Exception {
        mockMvc.perform(delete("/pub/deletePub")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pub)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePubTest_BAD_REQUEST() throws Exception {
        doThrow(BadRequestException.class).when(pubsService).deletePub(null);
        mockMvc.perform(delete("/pub/deletePub").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    PubDTO pub = TestUtil.generateMockPubDTO();
}
