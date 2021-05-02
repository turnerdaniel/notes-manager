package uk.co.danielturner.notesmanager.services;

import java.security.Principal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.co.danielturner.notesmanager.models.Token;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@Service
public class AccountService implements UserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtHelper jwtHelper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(""));

    return User
        .withUsername(account.getUsername())
        .password(account.getPassword())
        .authorities(new ArrayList<>())
        .build();
  }

  public Account create(Account account) {
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    return accountRepository.save(account);
  }

  public Token authenticate(Account account) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));
    } catch (BadCredentialsException e) {
      throw new RuntimeException("", e);
    }

    UserDetails userDetails = loadUserByUsername(account.getUsername());

    return new Token(jwtHelper.generateToken(userDetails));
  }

  public Account getDetails(Principal principal) {
    return accountRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new UsernameNotFoundException(""));
  }
}
