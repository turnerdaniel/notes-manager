package uk.co.danielturner.notesmanager.services;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.co.danielturner.notesmanager.errors.UsernameAlreadyExistsException;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private AccountRepository accountRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtHelper jwtHelper;
  @Mock private Principal principal;
  @InjectMocks private AccountService accountService;

  @Nested
  class LoadUser {

    @Test
    void callsFindByUsernameFromRepository() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(validAccount()));
      final String username = "example";

      accountService.loadUserByUsername(username);

      verify(accountRepository).findByUsername(username);
    }

    @Test
    void throwsUsernameNotFoundExceptionWhenUserDoesNotExist() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.empty());

      assertThatThrownBy(() -> accountService.loadUserByUsername("example"))
          .isInstanceOf(UsernameNotFoundException.class);
    }
  }

  @Nested
  class Register {

    @Test
    void callsSaveFromRepository() {
      Account account = validAccount();

      accountService.create(account);

      verify(accountRepository).save(any(Account.class));
    }

    @Test
    void encodesPasswordUsingEncoder() {
      Account account = validAccount();
      String password = account.getPassword();

      accountService.create(account);

      verify(passwordEncoder).encode(password);
    }

    @Test
    void ThrowsUsernameAlreadyExistsExceptionWhenAccountAlreadyExists() {
      DataIntegrityViolationException exception = new DataIntegrityViolationException("");
      when(accountRepository.save(any(Account.class))).thenThrow(exception);

      assertThatThrownBy(() -> accountService.create(validAccount()))
          .isInstanceOf(UsernameAlreadyExistsException.class)
          .hasCause(exception);
    }
  }

  @Nested
  class Authenticate {

    @Test
    void callsAuthenticationManager() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(validAccount()));
      Account account = validAccount();

      accountService.authenticate(account);
      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());

      verify(authenticationManager).authenticate(token);
    }

    @Test
    void callsTokenHelper() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(validAccount()));

      accountService.authenticate(validAccount());

      verify(jwtHelper).generateToken(any(Account.class));
    }

    @Test
    void throwsRuntimeExceptionWhenCredentialsDoNotMatch() {
      BadCredentialsException exception = new BadCredentialsException("");
      when(authenticationManager.authenticate(any(Authentication.class)))
          .thenThrow(exception);

      assertThatThrownBy(() -> accountService.authenticate(validAccount()))
          .isInstanceOf(RuntimeException.class)
          .hasCause(exception);
    }
  }

  @Nested
  class GetDetails {

    private final static String USERNAME = "username@company.com";

    @BeforeEach
    void setup() {
      when(principal.getName()).thenReturn(USERNAME);
    }

    @Test
    void callsFindByUsernameFromRepository() {
      when(principal.getName()).thenReturn(USERNAME);
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));

      accountService.getDetails(principal);

      verify(accountRepository).findByUsername(USERNAME);
    }

    @Test
    void throwsUsernameNotFoundExceptionWhenUserDoesNotExist() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.empty());

      assertThatThrownBy(() -> accountService.getDetails(principal))
          .isInstanceOf(UsernameNotFoundException.class);
    }
  }

  private static Account validAccount() {
    return new Account.Builder()
        .withUsername("example@company.com")
        .withPassword("passWORD")
        .build();
  }
}