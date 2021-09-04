package uk.co.danielturner.notesmanager.services;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.danielturner.notesmanager.errors.NoteNotFoundException;
import uk.co.danielturner.notesmanager.mappers.NoteMapper;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Note;
import uk.co.danielturner.notesmanager.models.dtos.NoteResponse;
import uk.co.danielturner.notesmanager.models.dtos.NoteRequest;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.repositories.NoteRepository;

@Service
public class NoteService {

  private final NoteRepository noteRepository;
  private final AccountRepository accountRepository;
  private final NoteMapper noteMapper;

  public NoteService(
      NoteRepository noteRepository,
      AccountRepository accountRepository,
      NoteMapper noteMapper) {
    this.noteRepository = noteRepository;
    this.accountRepository = accountRepository;
    this.noteMapper = noteMapper;
  }

  public NoteResponse create(NoteRequest request, Principal principal) {
    Account account = accountRepository
        .findByUsername(principal.getName())
        .orElseThrow(() -> new UsernameNotFoundException(""));
    Note note = noteMapper.convertToNote(request);
    note.setAccount(account);
    return noteMapper.convertToNoteResponse(noteRepository.save(note));
  }

  public List<NoteResponse> getAll(Principal principal) {
    List<Note> notes = noteRepository.findAllByAccount_Username(principal.getName());
    return notes.stream().map(noteMapper::convertToNoteResponse).collect(Collectors.toList());
  }

  public NoteResponse getById(Long id, Principal principal) {
    Note note = noteRepository
        .findByIdAndAccount_Username(id, principal.getName())
        .orElseThrow(() -> new NoteNotFoundException(id));
    return noteMapper.convertToNoteResponse(note);
  }

  public NoteResponse updateById(Long id, NoteRequest request, Principal principal) {
    Note note = noteRepository
        .findByIdAndAccount_Username(id, principal.getName())
        .map(updatedNote -> {
          updatedNote.setTitle(request.getTitle());
          updatedNote.setDescription(request.getDescription());
          return noteRepository.save(updatedNote);
        }).orElseThrow(() -> new NoteNotFoundException(id));
    return noteMapper.convertToNoteResponse(note);
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
