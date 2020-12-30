package uk.co.danielturner.notesmanager.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.co.danielturner.notesmanager.models.Note;

class NoteControllerTest {

    @Test
    void generatesNoteWithUniqueId() {
        NoteController noteController = new NoteController();
        Note note1 = noteController.createNote("","");
        Note note2 = noteController.createNote("","");

        assertThat(note1.getId()).isNotEqualTo(note2.getId());
    }

    @Test
    void createsNoteWithProvidedTitle() {
        final String title = "Example title";

        NoteController noteController = new NoteController();
        Note note = noteController.createNote(title, "");

        assertThat(note.getTitle()).isEqualTo(title);
    }

    @Test
    void createsNoteWithProvidedDescription() {
        final String description = "Example description that provides context.";

        NoteController noteController = new NoteController();
        Note note = noteController.createNote("", description);

        assertThat(note.getDescription()).isEqualTo(description);
    }

    @Test
    void createsNoteWithProvidedTitleAndDescription() {
        final String title = "Example title", description = "Example description";

        NoteController noteController = new NoteController();
        Note note = noteController.createNote(title, description);

        assertThat(note.getTitle()).isEqualTo(title);
        assertThat(note.getDescription()).isEqualTo(description);
    }
}