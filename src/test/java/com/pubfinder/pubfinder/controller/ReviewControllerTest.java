package com.pubfinder.pubfinder.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.exception.ReviewAlreadyExistsException;
import com.pubfinder.pubfinder.service.ReviewService;
import com.pubfinder.pubfinder.util.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(value = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReviewControllerTest {

  @MockBean
  private ReviewService reviewService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void saveTest() throws Exception {
    when(reviewService.saveReview(any(), any(), any())).thenReturn(reviewDTO);

    mockMvc.perform(post("/review/save/{pubId}/{username}", UUID.randomUUID(), "username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewDTO)))
        .andExpect(status().isCreated()).andDo(print());
  }

  @Test
  public void saveTest_NotFound() throws Exception {
    when(reviewService.saveReview(any(), any(), any())).thenThrow(ResourceNotFoundException.class);

    mockMvc.perform(post("/review/save/{pubId}/{username}", UUID.randomUUID(), "username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewDTO)))
        .andExpect(status().isNotFound()).andDo(print());
  }

  @Test
  public void saveTest_ReviewAlreadyExists() throws Exception {
    when(reviewService.saveReview(any(), any(), any())).thenThrow(
        ReviewAlreadyExistsException.class);

    mockMvc.perform(post("/review/save/{pubId}/{username}", UUID.randomUUID(), "username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewDTO)))
        .andExpect(status().isConflict());
  }

  @Test
  public void deleteTest() throws Exception {
    mockMvc.perform(delete("/review/delete/{id}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewDTO)))
        .andExpect(status().isNoContent());
  }

  @Test
  public void deletePubTest_NotFound() throws Exception {
    doThrow(ResourceNotFoundException.class).when(reviewService).deleteReview(any());
    mockMvc.perform(delete("/review/delete")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void editTest() throws Exception {
    when(reviewService.updateReview(any())).thenReturn(reviewDTO);

    mockMvc.perform(put("/review/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reviewDTO)))
        .andExpect(status().isOk());
  }

  @Test
  public void editPubTest_NotFound() throws Exception {
    when(reviewService.updateReview(any())).thenThrow(ResourceNotFoundException.class);

    mockMvc.perform(put("/pub/editPub")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  ReviewDto reviewDTO = TestUtil.generateMockReviewDTO();
}
