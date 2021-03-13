import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import org.springframework.mock.web.MockHttpServletResponse;

public class NotesManagerSteps extends Steps {

  MockHttpServletResponse response;

  @Given("^The client has successfully created (\\d*) notes$")
  public void setUpNotes(int quantity) throws Exception {
    setUpMultipleNotes(quantity);
  }

  @When("^The client sends a GET request to (\\S*)$")
  public void sendGetRequest(String url) throws Exception {
    this.response = get(url);
  }

  @When("^The client sends a POST request to (\\S*) with the body:$")
  public void sendPostRequest(String url, String body) throws Exception {
    this.response = post(url, body);
  }

  @When("^The client sends a PUT request to (\\S*) with the body:$")
  public void sendPutRequest(String url, String body) throws Exception {
    this.response = put(url, body);
  }

  @When("^The client sends a DELETE request to (\\S*)$")
  public void sendDeleteRequest(String url) throws Exception {
    this.response = delete(url);
  }

  @Then("^The client should get a (\\d*) response$")
  public void receiveResponseStatus(int responseCode) {
    assertThat(response.getStatus()).isEqualTo(responseCode);
  }

  @Then("^The response should contain a location header ending with (\\S*)$")
  public void receiveResponseLocation(String location) {
    assertThat(response.getHeader("Location")).endsWith(location);
  }

  @Then("^The response should contain a note with the title of \"(.*)\"$")
  public void receiveResponseBodyWithTitle(String title) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(response.getContentAsString());
    assertThat(responseNote.get("title").asText()).isEqualTo(title);
  }

  @Then("^The response should contain a note with the description of \"(.*)\"$")
  public void receiveResponseBodyWithDescription(String description) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(response.getContentAsString());
    assertThat(responseNote.get("description").asText()).isEqualTo(description);
  }

  @Then("^The response should contain a note with an ID of (\\d*)$")
  public void receiveResponseBodyWithId(int id) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(response.getContentAsString());
    assertThat(responseNote.get("id").asInt()).isEqualTo(id);
  }

  @Then("^The response should contain (\\d*) notes$")
  public void receiveResponseBodyWithQuantity(int quantity)
      throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNotes = createObject(response.getContentAsString());
    assertThat(responseNotes).size().isEqualTo(quantity);
  }
}
