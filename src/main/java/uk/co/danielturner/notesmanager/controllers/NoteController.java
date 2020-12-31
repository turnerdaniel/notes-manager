package uk.co.danielturner.notesmanager.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.services.NoteService;

@RestController
@RequestMapping("/v1")
public class NoteController {

  @Autowired
  private NoteService noteService;

  @PostMapping("/notes")
  public Note createNote(@RequestBody Note note) {
    return noteService.create(note);
  }

  @GetMapping("/notes")
  public List<Note> getAllNotes() {
    return noteService.getAll();
  }

  @GetMapping("/notes/{id}")
  public Note getNoteById(@PathVariable Long id) {
    return noteService.getById(id);
  }

  @DeleteMapping("/notes/{id}")
  public void deleteNoteById(@PathVariable Long id) {
    noteService.deleteById(id);
  }
}
