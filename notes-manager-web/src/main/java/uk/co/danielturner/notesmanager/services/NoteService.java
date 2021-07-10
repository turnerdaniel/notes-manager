package uk.co.danielturner.notesmanager.services;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@Service
public class NoteService {

  @Autowired
  private NoteRepository noteRepository;

  @Autowired
  private AccountRepository accountRepository;

  public Note create(Note note, Principal principal) {
    Account account = accountRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new UsernameNotFoundException(""));
    note.setAccount(account);
    return noteRepository.save(note);
  }

  public List<Note> getAll(Principal principal) {
    return noteRepository.findAllByAccount_Username(principal.getName());
  }

  public Note getById(Long id, Principal principal) {
    return noteRepository.findByIdAndAccount_Username(id, principal.getName()).orElseThrow(() -> new NoteNotFoundException(id));
  }

  public Note updateById(Long id, Note newNote, Principal principal) {
    return noteRepository.findByIdAndAccount_Username(id, principal.getName())
        .map(note -> {
          note.setTitle(newNote.getTitle());
          note.setDescription(newNote.getDescription());
          return noteRepository.save(note);
        }).orElseThrow(() -> new NoteNotFoundException(id));
  }

  @Transactional
  public void deleteById(Long id, Principal principal) {
    if (noteRepository.existsByIdAndAccount_Username(id, principal.getName())) {
      noteRepository.deleteByIdAndAccount_Username(id, principal.getName());
    } else {
      throw new NoteNotFoundException(id);
    }
  }
}
