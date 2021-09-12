package uk.co.danielturner.notesmanager.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.danielturner.notesmanager.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsername(String email);

  Optional<Account> findById(UUID id);
}
