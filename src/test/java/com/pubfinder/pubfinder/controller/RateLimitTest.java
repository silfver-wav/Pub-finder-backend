package com.pubfinder.pubfinder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void getTestTest() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/test").header("X-API-KEY", "pubaA6JW4SQsJX37jsLxWCnP72FUTChMuJlxuVWyqaiyACHt52rPzJpZFcyISf7Y")).andExpect(status().isOk());
        }

        mockMvc.perform(get("/test").header("X-API-KEY", "pubaA6JW4SQsJX37jsLxWCnP72FUTChMuJlxuVWyqaiyACHt52rPzJpZFcyISf7Y")).andExpect(status().isTooManyRequests());
    }
}