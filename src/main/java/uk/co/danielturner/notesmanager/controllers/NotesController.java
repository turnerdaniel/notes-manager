package uk.co.danielturner.notesmanager.controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.danielturner.notesmanager.models.Note;

@RestController
public class NotesController {
  private final AtomicLong uniqueId = new AtomicLong();

  @PostMapping("/create")
  public Note createNote(@RequestParam(value = "title", defaultValue = "Add title here") String title,
                         @RequestParam(value = "description", defaultValue = "") String description) {

    final long id = uniqueId.incrementAndGet();
    return new Note(id, title, description);
  }
}
