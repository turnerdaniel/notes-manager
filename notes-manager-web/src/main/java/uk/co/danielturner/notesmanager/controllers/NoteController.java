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
import uk.co.danielturner.notesmanager.models.dtos.NoteResponse;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;
import uk.co.danielturner.notesmanager.services.NoteService;

@RestController
@RequestMapping("/v2")
public class NoteController {

  private final NoteService noteService;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  @PostMapping("/notes")
  public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest request, Principal principal) {
    final NoteResponse response = noteService.create(request, principal);
    final URI uri = MvcUriComponentsBuilder
        .fromController(NoteController.class)
        .pathSegment("notes", "{id}")
        .build(response.getId());
    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping("/notes")
  public ResponseEntity<List<NoteResponse>> getAllNotes(Principal principal) {
    return ResponseEntity.ok(noteService.getAll(principal));
  }

  @GetMapping("/notes/{id}")
  public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long id, Principal principal) {
    return ResponseEntity.ok(noteService.getById(id, principal));
  }

  @PutMapping("/notes/{id}")
  public ResponseEntity<NoteResponse> updateNoteById(@PathVariable Long id, @RequestBody NoteRequest request, Principal principal) {
    return ResponseEntity.ok(noteService.updateById(id, request, principal));
  }

  @DeleteMapping("/notes/{id}")
  public ResponseEntity<Void> deleteNoteById(@PathVariable Long id, Principal principal) {
    noteService.deleteById(id, principal);
    return ResponseEntity.noContent().build();
  }
}
