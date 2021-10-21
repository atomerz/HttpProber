package dev.atomerz.httpprober.controller;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.atomerz.httpprober.entity.HttpService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class HttpProberControllerTest {

  @Autowired
  private HttpProberController controller;
  private RestClient restClient;

  @BeforeEach
  void beforeEach(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(documentationConfiguration(restDocumentation))
        .build();
    restClient = new RestClient(mockMvc);
  }

  @Test
  void testCrud() throws Exception {
    assertEquals(emptyList(), restClient.listServices());

    var google = restClient.addService(new HttpService("Google", "http://www.google.com"));
    assertEquals(google, restClient.getService(google.getId()));

    var twitter = restClient.addService(new HttpService("Twitter", "http://www.twitter.com"));
    assertEquals(Arrays.asList(google, twitter), restClient.listServices());

    google.setUrl("https://google.com");
    restClient.updateService(google);
    assertEquals(google, restClient.getService(google.getId()));

    restClient.deleteService(twitter.getId());
    assertEquals(Arrays.asList(google), restClient.listServices());
  }

  private static class RestClient {

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    public RestClient(MockMvc mockMvc) {
      this.mockMvc = mockMvc;
      this.mapper = new ObjectMapper();
    }

    public List<HttpService> listServices() throws Exception {
      return mapper.readValue(
          mockMvc.perform(get("/"))
              .andExpect(status().isOk())
              .andReturn().getResponse().getContentAsString(),
          new TypeReference<>() {
          });
    }

    public HttpService getService(long id) throws Exception {
      return mapper.readValue(
          mockMvc.perform(get("/" + id))
              .andExpect(status().isOk())
              .andReturn().getResponse().getContentAsString(),
          HttpService.class);
    }

    public HttpService addService(HttpService service) throws Exception {
      return mapper.readValue(
          mockMvc.perform(
              post("/")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(mapper.writeValueAsString(service)))
              .andExpect(status().isOk())
              .andReturn().getResponse().getContentAsString(),
          HttpService.class);
    }

    public HttpService updateService(HttpService service) throws Exception {
      return mapper.readValue(
          mockMvc.perform(
              put("/")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(mapper.writeValueAsString(service)))
              .andExpect(status().isOk())
              .andReturn().getResponse().getContentAsString(),
          HttpService.class);
    }

    public void deleteService(long id) throws Exception {
      mockMvc.perform(delete("/" + id))
          .andExpect(status().isOk())
          .andReturn();
    }
  }

}