package com.pubfinder.pubfinder.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/*
@SpringBootTest(properties = {
    "spring.datasource.url=",
    "spring.jpa.database-platform=",
    "spring.jpa.hibernate.ddl-auto=none",
})
@AutoConfigureMockMvc()
public class RateLimitTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PubsService pubsService;

  @Autowired
  private CacheManager cacheManager;


  @Test
  public void rateLimitTest() throws Exception {
    List<PubDto> pubs = new ArrayList<>(List.of(pub));
    when(pubsService.getPubs(1.0, 1.0, 1.0)).thenReturn(pubs);

    for (int i = 0; i < 50; i++) {
      mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(pubs)));
    }

    mockMvc.perform(get("/pub/getPubs/{lat}/{lng}/{radius}", 1.0, 1.0, 1.0))
        .andExpect(status().isTooManyRequests());
  }

  PubDto pub = TestUtil.generateMockPubDTO();
}

 */