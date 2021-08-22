package uk.co.danieltuner.notesmanager.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.StringJoiner;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public abstract class Steps {

  @Autowired private MockMvc mockMvc;
  @Autowired private DataSource dataSource;
  @Autowired private ObjectMapper objectMapper;

  protected MockHttpServletResponse get(String url) throws Exception {
    return sendRequest(MockMvcRequestBuilders.get(url));
  }

  protected MockHttpServletResponse get(String url, String jwt) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .get(url)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected MockHttpServletResponse post(String url, String body) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body));
  }

  protected MockHttpServletResponse post(String url, String body, String jwt) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected MockHttpServletResponse put(String url, String body) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body));
  }

  protected MockHttpServletResponse put(String url, String body, String jwt) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected MockHttpServletResponse delete(String url) throws Exception {
    return sendRequest(MockMvcRequestBuilders.delete(url));
  }

  protected MockHttpServletResponse delete(String url, String jwt) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .delete(url)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected JsonNode createObject(String json) throws JsonProcessingException {
    return objectMapper.readTree(json);
  }

  protected String createNoteRequest(String title, String description)
      throws JsonProcessingException {
    Map<String, String> request = Map.of(
        "title", title,
        "description", description
    );
    return objectMapper.writeValueAsString(request);
  }

  protected String createAccountRequest(String username, String password)
      throws JsonProcessingException {
    Map<String, String> request = Map.of(
        "username", username,
        "password", password
    );
    return objectMapper.writeValueAsString(request);
  }

  protected void setUpMultipleNotes(String token, int quantity) throws Exception {
    final String url = "/v2/notes";
    for (int i = 1; i < quantity + 1; i++) {
      String note = createNoteRequest("Bulk Title" + i, "Bulk Description" + i);
      post(url, note, token);
    }
  }

  protected void clearDatabase() throws SQLException {
    Connection conn = dataSource.getConnection();

    conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS=0");
    conn.createStatement().executeUpdate("ALTER SEQUENCE hibernate_sequence RESTART WITH 1");

    ResultSet rs = conn.createStatement().
        executeQuery(
            "SELECT table_name FROM information_schema.tables WHERE table_schema = SCHEMA()");
    while (rs.next()) {
      final String table = rs.getString(1);
      conn.createStatement().executeUpdate("TRUNCATE TABLE \"" + table + "\" RESTART IDENTITY");
    }
    rs.close();

    conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS=1");
    conn.close();
  }

  protected String changeTokenSubject(String token) throws IOException {
    String[] payload = token.split("\\.");

    JsonNode claims = objectMapper.readTree(Base64.getDecoder().decode(payload[1]));
    ((ObjectNode) claims).put("sub", "admin@example.com");

    return new StringJoiner(".")
        .add(payload[0])
        .add(Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(claims)))
        .add(payload[2])
        .toString();
  }

  private MockHttpServletResponse sendRequest(RequestBuilder requestBuilder) throws Exception {
    return mockMvc.perform(requestBuilder).andReturn().getResponse();
  }
}
