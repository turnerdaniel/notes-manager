package uk.co.danielturner.notesmanager.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO: Does this need exception or runtimeException? Exception requires "throws" keyword.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {

  // https://stackoverflow.com/questions/62044747/message-field-is-empty-in-error-response-spring-boot
  public NoteNotFoundException(Long id) {
    super("Could not find note with id of " + id.toString());
  }
}
