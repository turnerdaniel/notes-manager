package uk.co.danielturner.notesmanager.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that the Note being requested does not exist.
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {

  /**
   * Constructs a {@code NoteNotFoundException} with a detail message specifying
   * the exceptional id.
   * @param id The id which caused the exception
   * @apiNote Does not display error message to consumers due to <a href=https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#changes-to-the-default-error-pages-content>
   *   Changes to the Default Error Page's Content</a> in Spring Boot 2.3
   */
  public NoteNotFoundException(Long id) {
    super(String.format("Note with ID %d not found", id));
  }
}
