package uk.co.danielturner.notesmanager.controllers;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.services.NoteService;

@RestController
@RequestMapping("/v1")
public class NoteController {

  @Autowired
  private NoteService noteService;

  @PostMapping("/notes")
  public ResponseEntity<Note> createNote(@RequestBody Note note) {
    final Note createdNote = noteService.create(note);
    final URI uri = MvcUriComponentsBuilder
        .fromMethodCall(on(NoteController.class).createNote(note))
        .pathSegment("{id}")
        .buildAndExpand(createdNote.getId())
        .toUri();
    return ResponseEntity.created(uri).body(createdNote);
  }

  @GetMapping("/notes")
  public ResponseEntity<List<Note>> getAllNotes() {
    return ResponseEntity.ok(noteService.getAll());
  }

  @GetMapping("/notes/{id}")
  public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
    return ResponseEntity.ok(noteService.getById(id));
  }

  @PutMapping("/notes/{id}")
  public ResponseEntity<Note> updateNoteById(@PathVariable Long id, @RequestBody Note note) {
    return ResponseEntity.ok(noteService.updateById(id, note));
  }

  @DeleteMapping("/notes/{id}")
  public ResponseEntity<Note> deleteNoteById(@PathVariable Long id) {
    noteService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
