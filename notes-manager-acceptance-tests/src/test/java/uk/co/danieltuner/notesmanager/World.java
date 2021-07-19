package uk.co.danieltuner.notesmanager;

import io.cucumber.spring.ScenarioScope;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class World {

  MockHttpServletResponse response;
  String authToken;
}
