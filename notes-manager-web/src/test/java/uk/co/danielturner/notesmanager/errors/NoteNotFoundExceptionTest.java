package uk.co.danielturner.notesmanager.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NoteNotFoundExceptionTest {
  @Test
  void hasCorrectErrorMessageWhenExceptionIsInstantiated() {
    final long id = 12;
    final String expected = "Note with ID " + id + " not found";

    assertThat(new NoteNotFoundException(id).getMessage()).isEqualTo(expected);
  }
}
