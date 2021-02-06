package uk.co.danielturner.notesmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.danielturner.notesmanager.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {}
