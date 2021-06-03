package uk.co.danielturner.notesmanager.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
