package uk.co.danielturner.notesmanager.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

  List<Note> findAllByAccount_Username(String username);

  Optional<Note> findByIdAndAccount_Username(long id, String username);

  Boolean existsByIdAndAccount_Username(long id, String username);

  void deleteByIdAndAccount_Username(long id, String username);
}
