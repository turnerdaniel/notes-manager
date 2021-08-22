package uk.co.danielturner.notesmanager.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.List;
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
@RequestMapping("/v2")
public class NoteController {

  private final NoteService noteService;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  @PostMapping("/notes")
  public ResponseEntity<Note> createNote(@RequestBody Note note, Principal principal) {
    final Note createdNote = noteService.create(note, principal);
    final URI uri = MvcUriComponentsBuilder
        .fromController(NoteController.class)
        .pathSegment("notes", "{id}")
        .build(createdNote.getId());
    return ResponseEntity.created(uri).body(createdNote);
  }

  @GetMapping("/notes")
  public ResponseEntity<List<Note>> getAllNotes(Principal principal) {
    return ResponseEntity.ok(noteService.getAll(principal));
  }

  @GetMapping("/notes/{id}")
  public ResponseEntity<Note> getNoteById(@PathVariable Long id, Principal principal) {
    return ResponseEntity.ok(noteService.getById(id, principal));
  }

  @PutMapping("/notes/{id}")
  public ResponseEntity<Note> updateNoteById(@PathVariable Long id, @RequestBody Note note, Principal principal) {
    return ResponseEntity.ok(noteService.updateById(id, note, principal));
  }

  @DeleteMapping("/notes/{id}")
  public ResponseEntity<Note> deleteNoteById(@PathVariable Long id, Principal principal) {
    noteService.deleteById(id, principal);
    return ResponseEntity.noContent().build();
  }
}
