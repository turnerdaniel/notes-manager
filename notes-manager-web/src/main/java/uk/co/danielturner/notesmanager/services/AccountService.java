package uk.co.danielturner.notesmanager.services;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.co.danielturner.notesmanager.errors.UsernameAlreadyExistsException;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Token;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@Service
public class AccountService implements UserDetailsService {

  @Autowired private AccountRepository accountRepository;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtHelper jwtHelper;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public Account loadUserByUsername(String username) throws UsernameNotFoundException {
    return accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(""));
  }

  public Account create(Account account) {
    try {
      return accountRepository.save(new Account
          .Builder()
          .withUsername(account.getUsername())
          .withPassword(passwordEncoder.encode(account.getPassword()))
          .build());
    } catch (DataIntegrityViolationException e) {
      throw new UsernameAlreadyExistsException(e);
    }
  }

  public Token authenticate(Account account) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Account user = loadUserByUsername(account.getUsername());
    return new Token(jwtHelper.generateToken(user));
  }

  public Account getDetails(Principal principal) {
    return accountRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new UsernameNotFoundException(""));
  }
}
