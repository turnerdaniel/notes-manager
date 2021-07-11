import io.cucumber.junit.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import uk.co.danielturner.notesmanager.NotesManagerApplication;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@ContextConfiguration(classes = NotesManagerApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotesManagerAcceptanceTests {}
