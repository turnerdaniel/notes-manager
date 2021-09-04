package uk.co.danielturner.notesmanager.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.co.danielturner.notesmanager.models.dtos.NoteResponse;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;
import uk.co.danielturner.notesmanager.services.NoteService;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

  @Mock private NoteService noteService;
  @Mock private Principal principal;
  @InjectMocks private NoteController noteController;

  @Nested
  class NoteCreation {
    @BeforeEach
    void setup() {
      when(noteService.create(any(NoteRequest.class), any(Principal.class))).thenReturn(new NoteResponse());

      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void teardown() {
      RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void callsCreateFromService() {
      final NoteRequest note = new NoteRequest();

      noteController.createNote(note, principal);

      verify(noteService).create(note, principal);
    }

    @Test
    void returnsCreatedResponseOnSuccess() {
      ResponseEntity<NoteResponse> response = noteController.createNote(new NoteRequest(), principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void returnsLocationHeaderOnSuccess() {
      ResponseEntity<NoteResponse> response = noteController.createNote(new NoteRequest(), principal);

      assertThat(response.getHeaders().getLocation()).isNotNull();
    }
  }

  @Nested
  class RetrieveAllNotes {
    @Test
    void callsGetAllFromService() {
      noteController.getAllNotes(principal);

      verify(noteService).getAll(principal);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      ResponseEntity<List<NoteResponse>> response = noteController.getAllNotes(principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class RetrieveASingleNote {
    @Test
    void callsGetNotesByIdFromService() {
      final long id = 1;

      noteController.getNoteById(id, principal);

      verify(noteService).getById(id, principal);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      final long id = 1;

      ResponseEntity<NoteResponse> response = noteController.getNoteById(id, principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class UpdateASingleNote {
    @Test
    void callsUpdateNoteByIdFromService() {
      final long id = 1;
      final NoteRequest note = new NoteRequest();

      noteController.updateNoteById(id, note, principal);

      verify(noteService).updateById(id, note, principal);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      final long id = 1;

      ResponseEntity<NoteResponse> response = noteController.updateNoteById(id, new NoteRequest(), principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class DeleteASingleNote {
    @Test
    void callsDeleteNoteByIdFromService() {
      final long id = 1;

      noteController.deleteNoteById(id, principal);

      verify(noteService).deleteById(id, principal);
    }

    @Test
    void returnsNoContentResponseOnSuccess() {
      final long id = 1;

      ResponseEntity<?> response = noteController.deleteNoteById(id, principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
  }
}
