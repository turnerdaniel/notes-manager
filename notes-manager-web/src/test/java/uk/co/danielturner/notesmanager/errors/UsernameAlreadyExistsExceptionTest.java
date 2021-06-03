package uk.co.danielturner.notesmanager.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsernameAlreadyExistsExceptionTest {

  @Test
  void hasCorrectCause() {
    RuntimeException exception = new RuntimeException();

    assertThat(new UsernameAlreadyExistsException(exception)).hasCause(exception);
  }
}
