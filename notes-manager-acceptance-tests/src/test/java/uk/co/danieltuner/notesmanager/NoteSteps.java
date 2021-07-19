package uk.co.danieltuner.notesmanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class NoteSteps extends Steps {

  private final String title = "Title";
  private final String description = "Description";
  private final World world;

  public NoteSteps(World world) {
    this.world = world;
  }

  @Before
  public void clearDatabaseBeforeFeature() throws SQLException {
    clearDatabase();
  }

  @Given("^the client has successfully created (\\d*) note(?:s?)$")
  public void setUpNotes(int quantity) throws Exception {
    if (quantity == 1) {
      sendCreateNoteRequest();
    } else {
      setUpMultipleNotes(world.authToken, quantity);
    }
  }

  @When("^the client sends an authenticated request to create a new note$")
  public void sendCreateNoteRequest() throws Exception {
    final String url = "/v2/notes";
    world.response = post(url, createNoteAsJson(this.title, this.description), world.authToken);
  }

  @When("^the client sends an authenticated request to retrieve all notes$")
  public void sendGetAllNoteRequest() throws Exception {
    final String url = "/v2/notes";
    world.response = get(url, world.authToken);
  }

  @When("^the client sends an authenticated request to retrieve the (\\d*)(?:st|nd|rd|th) note$")
  public void sendGetSingleNoteRequest(int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    world.response = get(url, world.authToken);
  }

  @When("^the client sends an authenticated request to update the (\\d*)(?:st|nd|rd|th) note$")
  public void sendUpdateNoteRequest(int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    final String body = createNoteAsJson(this.title, this.description);
    world.response = put(url, body, world.authToken);
  }

  @When("^the client sends an authenticated request to delete the (\\d*)(?:st|nd|rd|th) note$")
  public void sendDeleteNoteRequest(int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    world.response = delete(url, world.authToken);
  }

  @Then("^The client should get a (\\d*) response$")
  public void receiveResponseStatus(int responseCode) {
    assertThat(world.response.getStatus()).isEqualTo(responseCode);
  }

  @Then("^the response should contain a location header ending with (\\S*)$")
  public void receiveResponseLocation(String location) {
    assertThat(world.response.getHeader("Location")).endsWith(location);
  }

  @Then("^the response should contain a note with a matching title$")
  public void receiveResponseBodyWithMatchingTitle() throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(world.response.getContentAsString());
    assertThat(responseNote.get("title").asText()).isEqualTo(title);
  }

  @Then("^the response should contain a note with a matching description$")
  public void receiveResponseBodyWithMatchingDescription() throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(world.response.getContentAsString());
    assertThat(responseNote.get("description").asText()).isEqualTo(description);
  }

  @Then("^The response should contain a note with the title of \"(.*)\"$")
  public void receiveResponseBodyWithTitle(String title) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(world.response.getContentAsString());
    assertThat(responseNote.get("title").asText()).isEqualTo(title);
  }

  @Then("^The response should contain a note with the description of \"(.*)\"$")
  public void receiveResponseBodyWithDescription(String description) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(world.response.getContentAsString());
    assertThat(responseNote.get("description").asText()).isEqualTo(description);
  }

  @Then("^The response should contain a note with an ID of (\\d*)$")
  public void receiveResponseBodyWithId(int id) throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNote = createObject(world.response.getContentAsString());
    assertThat(responseNote.get("id").asInt()).isEqualTo(id);
  }

  @Then("^the response should contain (\\d*) notes$")
  public void receiveResponseBodyWithQuantity(int quantity)
      throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNotes = createObject(world.response.getContentAsString());
    assertThat(responseNotes).size().isEqualTo(quantity);
  }
}
