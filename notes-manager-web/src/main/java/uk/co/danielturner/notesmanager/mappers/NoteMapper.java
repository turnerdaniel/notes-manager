package uk.co.danielturner.notesmanager.mappers;

import org.springframework.stereotype.Component;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.models.dtos.NoteResponse;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;

@Component
public class NoteMapper {
  public Note convertToNote(NoteRequest request) {
    if (request == null) {
      return null;
    }

    Note note = new Note();
    note.setTitle(request.getTitle());
    note.setDescription(request.getDescription());
    return note;
  }

  public NoteResponse convertToNoteResponse(Note note) {
    if (note == null) {
      return null;
    }

    NoteResponse response = new NoteResponse();
    response.setId(note.getId());
    response.setTitle(note.getTitle());
    response.setDescription(note.getDescription());
    response.setCreatedAt(note.getCreatedAt());
    response.setUpdatedAt(note.getUpdatedAt());
    return response;
  }
}
