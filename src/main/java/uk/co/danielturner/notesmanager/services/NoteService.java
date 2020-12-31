package uk.co.danielturner.notesmanager.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@Service
public class NoteService {

  @Autowired
  private NoteRepository noteRepository;

  public Note create(Note note) {
    return noteRepository.save(note);
  }

  public List<Note> getAll() {
    return noteRepository.findAll();
  }

  public Note getById(Long id) {
    return noteRepository.findById(id)
        .orElseThrow(
            () -> new NoteNotFoundException("Note not found with ID :: " + id.toString())
        );
  }

  public void deleteById(Long id) {
    noteRepository.deleteById(id);
  }
}
