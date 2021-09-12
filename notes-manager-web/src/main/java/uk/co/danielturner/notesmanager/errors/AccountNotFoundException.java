package uk.co.danielturner.notesmanager.errors;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(UUID id) {
    super(String.format("Account with ID: %s could not be found.", id.toString()));
  }
}
