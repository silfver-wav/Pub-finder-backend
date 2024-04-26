package com.pubfinder.pubfinder.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubfinder.pubfinder.dto.PubDto;
import com.pubfinder.pubfinder.service.PubsService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
@AutoConfigureMockMvc()
public class RateLimitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PubsService pubsService;

    @Test
    public void rateLimitTest() throws Exception {
        List<PubDto> pubs = new ArrayList<>(List.of(pub));
        when(pubsService.getPubs(1.0,1.0,1.0)).thenReturn(pubs);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(pubs)));
        }

        mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0)).andExpect(status().isTooManyRequests());
    }
    PubDto pub = TestUtil.generateMockPubDTO();
}