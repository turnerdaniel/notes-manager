package uk.co.danielturner.notesmanager.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.services.NoteService;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

  @Mock
  NoteService noteService;

  @InjectMocks
  NoteController noteController;

  @Nested
  class createNoteTests {
    @BeforeEach
    void setup() {
      when(noteService.create(any(Note.class))).thenReturn(new Note());

      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void teardown() {
      RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void callsCreateFromService() {
      final Note note = new Note();

      noteController.createNote(note);

      verify(noteService).create(note);
    }

    @Test
    void returnsCreatedResponseOnSuccess() {
      ResponseEntity response = noteController.createNote(new Note());

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void returnsLocationHeaderOnSuccess() {
      ResponseEntity response = noteController.createNote(new Note());

      assertThat(response.getHeaders().getLocation()).isNotNull();
    }
  }

  @Nested
  class getAllNotesTests {
    @Test
    void callsGetAllFromService() {
      noteController.getAllNotes();

      verify(noteService).getAll();
    }

    @Test
    void returnsOkResponseOnSuccess() {
      ResponseEntity response = noteController.getAllNotes();

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class getNoteByIdTests {
    @Test
    void callsGetNotesByIdFromService() {
      final long id = 1;

      noteController.getNoteById(id);

      verify(noteService).getById(id);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      final long id = 1;

      ResponseEntity response = noteController.getNoteById(id);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class updateNoteByIdTests {
    @Test
    void callsUpdateNoteByIdFromService() {
      final long id = 1;
      final Note note = new Note();

      noteController.updateNoteById(id, note);

      verify(noteService).updateById(id, note);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      final long id = 1;

      ResponseEntity response = noteController.updateNoteById(id, new Note());

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class deleteNoteByIdTests {
    @Test
    void callsDeleteNoteByIdFromService() {
      final long id = 1;

      noteController.deleteNoteById(id);

      verify(noteService).deleteById(id);
    }

    @Test
    void returnsNoContentResponseOnSuccess() {
      final long id = 1;

      ResponseEntity response = noteController.deleteNoteById(id);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
  }
}
