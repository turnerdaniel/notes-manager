import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.co.danielturner.notesmanager.NotesManagerApplication;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration(classes = NotesManagerApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS) //TODO: investigate transactions for clearing DB
@AutoConfigureMockMvc
public abstract class Steps {

  @Autowired
  MockMvc mockMvc;

  protected MockHttpServletResponse get(String url) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.get(url))
        .andReturn()
        .getResponse();
  }

  protected MockHttpServletResponse post(String url, String body) throws Exception {
   return mockMvc.perform(
       MockMvcRequestBuilders.post(url)
       .contentType(MediaType.APPLICATION_JSON)
       .content(body))
       .andReturn()
       .getResponse();
  }

  protected MockHttpServletResponse put(String url, String body) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.put(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andReturn()
        .getResponse();
  }

  protected MockHttpServletResponse delete(String url) throws Exception {
    return mockMvc.perform(
        MockMvcRequestBuilders.delete(url))
        .andReturn()
        .getResponse();
  }

  protected JsonNode createObject(String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(json);
  }

  protected void setUpMultipleNotes(int quantity) throws Exception {
    final String url = "/v1/notes";
    final String noteTemplate = "{\"title\": \"Example Title %d\"}";
    for (int i = 1; i < quantity + 1; i++) {
      post(url, String.format(noteTemplate, i));
    }
  }
}
