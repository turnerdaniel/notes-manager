package uk.co.danielturner.notesmanager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

  @Mock
  private NoteRepository noteRepository;

  @InjectMocks
  private NoteService noteService;

  @Test
  void callsSaveFromRepository() {
    final Note note = new Note();

    noteService.create(note);

    verify(noteRepository).save(note);
  }

  @Test
  void callsFindAllFromRepository() {
    noteService.getAll();

    verify(noteRepository).findAll();
  }

  @Test
  void callsFindByIdFromRepository() {
    when(noteRepository.findById(any(Long.class))).thenReturn(Optional.of(new Note()));
    final long id = 1;

    noteService.getById(id);

    verify(noteRepository).findById(id);
  }

  @Test
  void throwsExceptionWhenIdIsNotFound() {
    when(noteRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    final long id = -1;

    assertThatExceptionOfType(NoteNotFoundException.class)
        .isThrownBy(() -> noteService.getById(id));
  }

  @Test
  void callsFindByIdAndSaveFromRepository() {
    when(noteRepository.findById(anyLong())).thenReturn(Optional.of(new Note()));
    when(noteRepository.save(any(Note.class))).then(returnsFirstArg());
    final Note note = new Note();
    final long id = 1;

    noteService.updateById(id, note);

    verify(noteRepository).findById(id);
    verify(noteRepository).save(any(Note.class));
  }

  @Test
  void returnsUpdatedNoteWithCorrectFields() {
    when(noteRepository.findById(anyLong())).thenReturn(Optional.of(new Note()));
    when(noteRepository.save(any(Note.class))).then(returnsFirstArg());
    final String title = "Title", description = "Description";
    final Note note = new Note(title, description);
    final long id = 1;

    Note response = noteService.updateById(id, note);

    assertThat(response.getTitle()).isEqualTo(title);
    assertThat(response.getDescription()).isEqualTo(description);
  }

  @Test
  void throwsExceptionWhenUpdatedIdIsNotFound() {
    when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());
    final long id = -1;
    Note note = new Note();

    assertThatExceptionOfType(NoteNotFoundException.class)
        .isThrownBy(() -> noteService.updateById(id, note));
  }

  @Test
  void callsDeleteByIdFromRepository() {
    when(noteRepository.existsById(anyLong())).thenReturn(true);
    final long id = 1;

    noteService.deleteById(id);

    verify(noteRepository).deleteById(id);
  }

  @Test
  void throwsExceptionWhenDeletionIdIsNotFound() {
    when(noteRepository.existsById(anyLong())).thenReturn(false);
    final long id = 1;

    assertThatExceptionOfType(NoteNotFoundException.class)
        .isThrownBy(() -> noteService.deleteById(id));
  }
}
