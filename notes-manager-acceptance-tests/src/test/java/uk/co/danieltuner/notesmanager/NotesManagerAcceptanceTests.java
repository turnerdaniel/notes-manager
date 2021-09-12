package uk.co.danieltuner.notesmanager;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import uk.co.danielturner.notesmanager.NotesManagerApplication;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
@CucumberContextConfiguration
@ContextConfiguration(classes = NotesManagerApplication.class)
@ComponentScan
@SpringBootTest
@AutoConfigureMockMvc
public class NotesManagerAcceptanceTests {}
