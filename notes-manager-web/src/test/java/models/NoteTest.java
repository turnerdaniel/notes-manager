package uk.co.danielturner.notesmanager.models;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NoteTest {
  @Test
  void createsNoteObjectWithNullFieldsUsingDefaultConstructor() {
    Note note = new Note();

    assertThat(note).hasAllNullFieldsOrPropertiesExcept("id");
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
}
