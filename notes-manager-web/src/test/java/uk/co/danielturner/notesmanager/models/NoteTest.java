package uk.co.danielturner.notesmanager.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

class NoteTest {
  @Test
  void createsNoteObjectWithEmptyFieldsUsingDefaultConstructor() {
    Note note = new Note();

    assertThat(note.getTitle()).isEmpty();
    assertThat(note.getDescription()).isEmpty();
  }

  @Test
  void createsNoteObjectWithCorrectPropertiesUsingTheParametrisedConstructor() {
    final String title = "Title", description = "Description";
    Note note = new Note(title, description);

    assertThat(note.getTitle()).isEqualTo(title);
    assertThat(note.getDescription()).isEqualTo(description);
  }

  @Test
  void setsTitleToProvidedValue() {
    final String title = "Title";
    Note note = new Note();
    note.setTitle(title);

    assertThat(note.getTitle()).isEqualTo(title);
  }

  @Test
  void setsTitleToDefaultValueWhenNull() {
    Note note = new Note();
    note.setTitle(null);

    assertThat(note.getTitle()).isNotNull();
  }

  @Test
  void setsDescriptionToProvidedValue() {
    final String description = "Description";
    Note note = new Note();
    note.setDescription(description);

    assertThat(note.getDescription()).isEqualTo(description);
  }

  @Test
  void setsDescriptionToDefaultValueWhenNull() {
    Note note = new Note();
    note.setDescription(null);

    assertThat(note.getDescription()).isNotNull();
  }

  @Test
  void setsCreatedAtToCurrentTime() {
    final Date now = new Date();
    Note note = new Note("", "");

    assertThat(note.getCreatedAt()).isCloseTo(now, 5000);
  }

  @Test
  void setsUpdatedAtToCurrentTime() {
    final Date now = new Date();
    Note note = new Note("", "");

    assertThat(note.getUpdatedAt()).isCloseTo(now, 5000);
  }
}
