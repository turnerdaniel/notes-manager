package uk.co.danielturner.notesmanager.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.models.dtos.NoteResponse;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;

class NoteMapperTest {

  private final NoteMapper noteMapper = new NoteMapper();

  @Test
  void convertRequestToNoteCorrectly() {
    final String title = "Title", description = "Description";
    NoteRequest request = new NoteRequest.Builder()
        .withTitle(title)
        .withDescription(description)
        .build();

    Note note = noteMapper.convertToNote(request);

    assertThat(note.getTitle()).isEqualTo(title);
    assertThat(note.getDescription()).isEqualTo(description);
  }

  @Test
  void convertRequestWithNullTitleToNoteCorrectly() {
    NoteRequest request = new NoteRequest.Builder().withDescription("").build();

    Note note = noteMapper.convertToNote(request);

    assertThat(note.getTitle()).isEmpty();
  }

  @Test
  void convertRequestWithNullDescriptionToNoteCorrectly() {
    NoteRequest request = new NoteRequest.Builder().withTitle("").build();

    Note note = noteMapper.convertToNote(request);

    assertThat(note.getDescription()).isEmpty();
  }

  @Test
  void convertRequestWithNullTitleAndDescriptionToNoteCorrectly() {
    NoteRequest request = new NoteRequest();

    Note note = noteMapper.convertToNote(request);

    assertThat(note.getTitle()).isEmpty();
    assertThat(note.getDescription()).isEmpty();
  }

  @Test
  void returnsNullWhenNoteRequestIsNull() {
    assertThat(noteMapper.convertToNote(null)).isNull();
  }

  @Test
  void convertsNoteToResponseCorrectly() {
    final String title = "Title";
    final String description = "Description";
    Note note = new Note(title, description);

    NoteResponse response = noteMapper.convertToNoteResponse(note);

    assertThat(response.getTitle()).isEqualTo(title);
    assertThat(response.getDescription()).isEqualTo(description);
  }

  @Test
  void returnsNullResponseWhenNoteIsNull() {
    assertThat(noteMapper.convertToNoteResponse(null)).isNull();
  }
}
