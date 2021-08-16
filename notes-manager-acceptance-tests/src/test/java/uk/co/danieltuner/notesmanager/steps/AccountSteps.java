package uk.co.danieltuner.notesmanager.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.io.IOException;
import uk.co.danieltuner.notesmanager.utils.World;

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

    world.response = post("/v2/register", createAccountRequest(this.username, this.password));
  }

  @Given("^the client has authenticated with a different user account$")
  public void authenticateWithDifferentUserAccount() throws Exception {
    this.username = "real.user@domain.com";
    this.password = "$tr0nGP455W0Rd";

    post("/v2/register", createAccountRequest(this.username, this.password));
    world.response = post("/v2/authenticate", createAccountRequest(this.username, this.password));
    world.authToken = createObject(world.response.getContentAsString()).get("token").asText();
  }

  @When("^the client has authenticated$")
  public void attemptLogin() throws Exception {
    world.response = post("/v2/authenticate", createAccountRequest(this.username, this.password));
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
