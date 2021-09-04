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
import uk.co.danielturner.notesmanager.mappers.AccountMapper;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.dtos.TokenResponse;
import uk.co.danielturner.notesmanager.models.dtos.AccountRequest;
import uk.co.danielturner.notesmanager.models.dtos.AccountResponse;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@Service
public class AccountService implements UserDetailsService {

  @Autowired private AccountRepository accountRepository;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtHelper jwtHelper;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AccountMapper accountMapper;

  @Override
  public Account loadUserByUsername(String username) throws UsernameNotFoundException {
    return accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(""));
  }

  public AccountResponse create(AccountRequest request) {
    Account account = new Account.Builder()
        .withUsername(request.getUsername())
        .withPassword(passwordEncoder.encode(request.getPassword()))
        .build();
    try {
      account = accountRepository.save(account);
    } catch (DataIntegrityViolationException e) {
      throw new UsernameAlreadyExistsException(e);
    }
    return accountMapper.convertToAccountResponse(account);
  }

  public TokenResponse authenticate(AccountRequest request) {
    try {
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Account account = loadUserByUsername(request.getUsername());
    return new TokenResponse(jwtHelper.generateToken(account));
  }

  public AccountResponse getDetails(Principal principal) {
    Account account = accountRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new UsernameNotFoundException(""));
    return accountMapper.convertToAccountResponse(account);
  }
}
