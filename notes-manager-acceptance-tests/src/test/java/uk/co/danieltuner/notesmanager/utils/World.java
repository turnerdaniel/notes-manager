package uk.co.danieltuner.notesmanager.utils;

import io.cucumber.spring.ScenarioScope;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class World {

  public MockHttpServletResponse response;
  public String authToken;
}
