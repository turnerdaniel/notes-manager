package uk.co.danielturner.notesmanager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

  private static final String USERNAME = "username";

  @Mock private NoteRepository noteRepository;
  @Mock private Principal principal;
  @Mock private AccountRepository accountRepository;

  @InjectMocks private NoteService noteService;

  @BeforeEach
  void setup() {
    when(principal.getName()).thenReturn(USERNAME);
  }

  @Nested
  class NoteCreation {
    @Test
    void callsSaveFromRepository() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));
      final Note note = new Note();

      noteService.create(note, principal);

      verify(noteRepository).save(note);
    }

    @Test
    void attachesAccountToNoteDuringSave() {
      ArgumentCaptor<Note> noteCaptor = ArgumentCaptor.forClass(Note.class);
      final Account account = new Account();
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

      noteService.create(new Note(), principal);

      verify(noteRepository).save(noteCaptor.capture());
      assertThat(noteCaptor.getValue().getAccount()).isEqualTo(account);
    }

    @Test
    void throwsNotFoundExceptionWhenAccountNotFound() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.empty());

      assertThatExceptionOfType(UsernameNotFoundException.class)
          .isThrownBy(() -> noteService.create(new Note(), principal));
    }
  }

  @Nested
  class RetrieveAllNotes {
    @Test
    void callsFindAllNotesByAccountFromRepository() {
      noteService.getAll(principal);

      verify(noteRepository).findAllByAccount_Username(USERNAME);
    }
  }

  @Nested
  class RetrieveASingleNote {
    @Test
    void callsFindByIdAndAccountFromRepository() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString()))
          .thenReturn(Optional.of(new Note()));
      final long id = 1;

      noteService.getById(id, principal);

      verify(noteRepository).findByIdAndAccount_Username(id, USERNAME);
    }

    @Test
    void throwsExceptionWhenIdIsNotFound() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString())).thenReturn(Optional.empty());

      assertThatExceptionOfType(NoteNotFoundException.class)
          .isThrownBy(() -> noteService.getById(-1L, principal));
    }
  }

  @Nested
  class UpdateASingleNote {

    @BeforeEach
    void setup() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString()))
          .thenReturn(Optional.of(new Note()));
      lenient().when(noteRepository.save(any(Note.class))).then(returnsFirstArg());
    }

    @Test
    void callsFindByIdAndAccountFromRepository() {
      final long id = 1;

      noteService.updateById(id, new Note(), principal);

      verify(noteRepository).findByIdAndAccount_Username(id, USERNAME);
    }

    @Test
    void callsSaveFromRepository() {
      noteService.updateById(1L, new Note(), principal);

      verify(noteRepository).save(any(Note.class));
    }

    @Test
    void returnsUpdatedNoteWithCorrectTitle() {
      final String title = "Title";
      final Note note = new Note(title,"");

      Note response = noteService.updateById(1L, note, principal);

      assertThat(response.getTitle()).isEqualTo(title);
    }

    @Test
    void returnsUpdatedNoteWithCorrectDescription() {
      final String description = "Title";
      final Note note = new Note("",description);

      Note response = noteService.updateById(1L, note, principal);

      assertThat(response.getDescription()).isEqualTo(description);
    }

    @Test
    void throwsExceptionWhenUpdatedIdIsNotFound() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString()))
          .thenReturn(Optional.empty());

      assertThatExceptionOfType(NoteNotFoundException.class)
          .isThrownBy(() -> noteService.updateById(-1L, new Note(), principal));
    }
  }

  @Nested
  class DeleteASingleNote {

    @BeforeEach
    void setup() {
      when(noteRepository.existsByIdAndAccount_Username(anyLong(), anyString())).thenReturn(true);
    }

    @Test
    void checksIfNoteExists() {
      final long id = 1;

      noteService.deleteById(id, principal);

      verify(noteRepository).existsByIdAndAccount_Username(id, USERNAME);
    }

    @Test
    void callsDeleteByIdAndAccountFromRepository() {
      final long id = 1;

      noteService.deleteById(id, principal);

      verify(noteRepository).deleteByIdAndAccount_Username(id, USERNAME);
    }

    @Test
    void throwsExceptionWhenNoteNotFound() {
      when(noteRepository.existsByIdAndAccount_Username(anyLong(), anyString())).thenReturn(false);

      assertThatExceptionOfType(NoteNotFoundException.class)
          .isThrownBy(() -> noteService.deleteById(-1L, principal));
    }
  }
}
