package uk.co.danieltuner.notesmanager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;

public class AccountSteps extends Steps {

  private final World world;
  private String username;
  private String password;

  public AccountSteps(World world) {
    this.world = world;
  }

  @Given("^an existing user account$")
  @When("^the client sends a request to create a new account$")
  public void createUserAccount() throws Exception {
    this.username = "test.user@example.com";
    this.password = "PaSsWoRd";
    final String body = String
        .format("{\"username\": \"%s\", \"password\": \"%s\"}", this.username, this.password);

    world.response = post("/v2/register", body);
  }

  @When("^the client has authenticated$")
  public void attemptLogin() throws Exception {
    final String body = String
        .format("{\"username\": \"%s\", \"password\": \"%s\"}", this.username, this.password);

    world.response = post("/v2/authenticate", body);
    world.authToken = createObject(world.response.getContentAsString()).get("token").asText();
  }

  @When("^the client sends an (unauthenticated|authenticated) request for account details$")
  public void sendAccountDetailsRequest(String auth) throws Exception {
    final String url = "/v2/account";
    world.response = auth.equals("authenticated") ? get(url, world.authToken) : get(url);
  }

  @When("^the subject within the authentication token is altered$")
  public void changeAuthTokenClaim() throws IOException {
    world.authToken = changeTokenSubject(world.authToken);
  }
}
