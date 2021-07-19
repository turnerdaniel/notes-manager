package uk.co.danieltuner.notesmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.co.danielturner.notesmanager.models.Note;

public abstract class Steps {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  DataSource dataSource;

  @Autowired
  ObjectMapper objectMapper;

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

  protected MockHttpServletResponse put(String url, String body, String jwt) throws Exception {
    return sendRequest(MockMvcRequestBuilders
        .put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected MockHttpServletResponse delete(String url, String jwt) throws Exception {
    return sendRequest(
        MockMvcRequestBuilders.delete(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
  }

  protected JsonNode createObject(String json) throws JsonProcessingException {
    return objectMapper.readTree(json);
  }

  protected String createNoteAsJson(String title, String description)
      throws JsonProcessingException {
    return objectMapper.writeValueAsString(new Note(title, description));
  }

  protected void setUpMultipleNotes(String token, int quantity) throws Exception {
    final String url = "/v2/notes";
    for (int i = 1; i < quantity + 1; i++) {
      String note = createNoteAsJson("Bulk Title" + i, "Bulk Description" + i);
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

  private MockHttpServletResponse sendRequest(RequestBuilder requestBuilder) throws Exception {
    return mockMvc.perform(requestBuilder).andReturn().getResponse();
  }
}
