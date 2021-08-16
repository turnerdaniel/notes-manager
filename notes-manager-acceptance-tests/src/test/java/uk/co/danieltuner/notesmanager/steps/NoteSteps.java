package uk.co.danieltuner.notesmanager.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import uk.co.danieltuner.notesmanager.utils.World;

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
      sendCreateNoteRequest("authenticated");
    } else {
      setUpMultipleNotes(world.authToken, quantity);
    }
  }

  @When("^the client sends an (unauthenticated|authenticated) request to create a new note$")
  public void sendCreateNoteRequest(String auth) throws Exception {
    final String url = "/v2/notes";
    final String note = createNoteRequest(this.title, this.description);
    world.response = auth.equals("authenticated") ? post(url, note, world.authToken) : post(url, note);
  }

  @When("^the client sends an (unauthenticated|authenticated) request to retrieve all notes$")
  public void sendGetAllNoteRequest(String auth) throws Exception {
    final String url = "/v2/notes";
    world.response = auth.equals("authenticated") ? get(url, world.authToken) : get(url);
  }

  @When("^the client sends an (unauthenticated|authenticated) request to retrieve the (\\d*)(?:st|nd|rd|th) note$")
  public void sendGetSingleNoteRequest(String auth, int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    world.response = auth.equals("authenticated") ? get(url, world.authToken) : get(url);
  }

  @When("^the client sends an (unauthenticated|authenticated) request to update the (\\d*)(?:st|nd|rd|th) note$")
  public void sendUpdateNoteRequest(String auth, int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    final String body = createNoteRequest(this.title, this.description);
    world.response = auth.equals("authenticated") ? put(url, body, world.authToken) : put(url, body);
  }

  @When("^the client sends an (unauthenticated|authenticated) request to delete the (\\d*)(?:st|nd|rd|th) note$")
  public void sendDeleteNoteRequest(String auth, int id) throws Exception {
    final String url = String.format("/v2/notes/%d", id);
    world.response = auth.equals("authenticated") ? delete(url, world.authToken) : delete(url);
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

  @Then("^the response should contain (\\d*) notes$")
  public void receiveResponseBodyWithQuantity(int quantity)
      throws UnsupportedEncodingException, JsonProcessingException {
    JsonNode responseNotes = createObject(world.response.getContentAsString());
    assertThat(responseNotes).size().isEqualTo(quantity);
  }
}
