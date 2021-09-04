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
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.mappers.NoteMapper;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

  private static final String USERNAME = "username";

  @Mock private NoteRepository noteRepository;
  @Mock private Principal principal;
  @Mock private AccountRepository accountRepository;
  @Mock private NoteMapper noteMapper;
  @InjectMocks private NoteService noteService;
  @Captor private ArgumentCaptor<Note> noteCaptor;

  @BeforeEach
  void setup() {
    when(principal.getName()).thenReturn(USERNAME);
  }

  @Nested
  class CreateNote {

    @Test
    void noteIsSavedToRepository() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));
      Note note = new Note();
      when(noteMapper.convertToNote(any(NoteRequest.class))).thenReturn(note);
      final NoteRequest request = new NoteRequest();

      noteService.create(request, principal);

      verify(noteRepository).save(note);
    }

    @Test
    void attachesCorrectAccountToNote() {
      when(noteMapper.convertToNote(any(NoteRequest.class))).thenReturn(new Note());
      final Account account = new Account();
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

      noteService.create(new NoteRequest(), principal);

      verify(noteRepository).save(noteCaptor.capture());
      assertThat(noteCaptor.getValue().getAccount()).isEqualTo(account);
    }

    @Test
    void throwsNotFoundExceptionWhenAccountNotFound() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.empty());

      assertThatExceptionOfType(UsernameNotFoundException.class)
          .isThrownBy(() -> noteService.create(new NoteRequest(), principal));
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
  class RetrieveSingleNote {
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
  class UpdateSingleNote {

    @BeforeEach
    void setup() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString()))
          .thenReturn(Optional.of(new Note()));
      lenient().when(noteRepository.save(any(Note.class))).then(returnsFirstArg());
    }

    @Test
    void callsFindByIdAndAccountFromRepository() {
      final long id = 1;

      noteService.updateById(id, new NoteRequest(), principal);

      verify(noteRepository).findByIdAndAccount_Username(id, USERNAME);
    }

    @Test
    void callsSaveFromRepository() {
      noteService.updateById(1L, new NoteRequest(), principal);

      verify(noteRepository).save(any(Note.class));
    }

    @Test
    void callsSaveWithCorrectTitle() {
      final String title = "Title";
      final NoteRequest request = new NoteRequest.Builder().withTitle(title).build();

      noteService.updateById(1L, request, principal);

      verify(noteRepository).save(noteCaptor.capture());
      assertThat(noteCaptor.getValue().getTitle()).isEqualTo(title);
    }

    @Test
    void callsSaveWithCorrectDescription() {
      final String description = "Description";
      final NoteRequest request = new NoteRequest.Builder().withDescription(description).build();

      noteService.updateById(1L, request, principal);

      verify(noteRepository).save(noteCaptor.capture());
      assertThat(noteCaptor.getValue().getDescription()).isEqualTo(description);
    }

    @Test
    void throwsExceptionWhenUpdatedIdIsNotFound() {
      when(noteRepository.findByIdAndAccount_Username(anyLong(), anyString()))
          .thenReturn(Optional.empty());

      assertThatExceptionOfType(NoteNotFoundException.class)
          .isThrownBy(() -> noteService.updateById(-1L, new NoteRequest(), principal));
    }
  }

  @Nested
  class DeleteSingleNote {

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
