package uk.co.danielturner.notesmanager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import uk.co.danielturner.notesmanager.errors.AccountNotFoundException;
import uk.co.danielturner.notesmanager.errors.UsernameAlreadyExistsException;
import uk.co.danielturner.notesmanager.mappers.AccountMapper;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.dtos.AccountRequest;
import uk.co.danielturner.notesmanager.repositories.AccountRepository;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock private AccountRepository accountRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtHelper jwtHelper;
  @Mock private Principal principal;
  @Mock private AccountMapper accountMapper;
  @InjectMocks private AccountService accountService;

  @Nested
  class LoadUserByUsername {

    @Test
    void callsFindByUsernameFromRepository() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));
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
  class LoadUserById {
    @Test
    void callsFindByIdFromRepository() {
      when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.of(new Account()));
      final UUID id = UUID.randomUUID();

      accountService.loadUserById(id);

      verify(accountRepository).findById(id);
    }

    @Test
    void throwsAccountNotFoundExceptionWhenUserDoesNotExist() {
      when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
      final UUID id = UUID.randomUUID();

      assertThatThrownBy(() -> accountService.loadUserById(id))
          .isInstanceOf(AccountNotFoundException.class)
          .hasMessageContaining(id.toString());
    }
  }

  @Nested
  class Register {

    @Test
    void callsSaveFromRepository() {
      AccountRequest request = validAccountRequest();

      accountService.create(request);

      verify(accountRepository).save(any(Account.class));
    }

    @Test
    void encodesPasswordUsingEncoder() {
      AccountRequest request = validAccountRequest();
      String password = request.getPassword();

      accountService.create(request);

      verify(passwordEncoder).encode(password);
    }

    @Test
    void callsAccountMapperWhenReturningValue() {
      when(accountRepository.save(any(Account.class))).then(returnsFirstArg());
      AccountRequest request = validAccountRequest();

      accountService.create(request);

      verify(accountMapper).convertToAccountResponse(any(Account.class));
    }

    @Test
    void ThrowsUsernameAlreadyExistsExceptionWhenAccountAlreadyExists() {
      DataIntegrityViolationException exception = new DataIntegrityViolationException("");
      when(accountRepository.save(any(Account.class))).thenThrow(exception);

      assertThatThrownBy(() -> accountService.create(validAccountRequest()))
          .isInstanceOf(UsernameAlreadyExistsException.class)
          .hasCause(exception);
    }
  }

  @Nested
  class Authenticate {

    @Captor ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor;

    @Test
    void callsAuthenticationManager() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));
      AccountRequest request = validAccountRequest();

      accountService.authenticate(request);

      verify(authenticationManager).authenticate(tokenCaptor.capture());
      assertThat(tokenCaptor.getValue().getPrincipal()).hasToString(request.getUsername());
      assertThat(tokenCaptor.getValue().getCredentials()).hasToString(request.getPassword());
    }

    @Test
    void callsTokenHelper() {
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(new Account()));

      accountService.authenticate(validAccountRequest());

      verify(jwtHelper).generateToken(any(Account.class));
    }

    @Test
    void throwsRuntimeExceptionWhenCredentialsDoNotMatch() {
      BadCredentialsException exception = new BadCredentialsException("");
      when(authenticationManager.authenticate(any(Authentication.class)))
          .thenThrow(exception);

      assertThatThrownBy(() -> accountService.authenticate(validAccountRequest()))
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

    @Test
    void callsAccountMapperWhenReturningValue() {
      when(principal.getName()).thenReturn(USERNAME);
      Account account = new Account();
      when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

      accountService.getDetails(principal);

      verify(accountMapper).convertToAccountResponse(account);
    }
  }

  private static AccountRequest validAccountRequest() {
    AccountRequest request = new AccountRequest();
    request.setUsername("example@company.com");
    request.setPassword("passWORD");
    return request;
  }
}
