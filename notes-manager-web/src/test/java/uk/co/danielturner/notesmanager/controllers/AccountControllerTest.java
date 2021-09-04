package uk.co.danielturner.notesmanager.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.security.Principal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.co.danielturner.notesmanager.models.dtos.TokenResponse;
import uk.co.danielturner.notesmanager.models.dtos.AccountRequest;
import uk.co.danielturner.notesmanager.models.dtos.AccountResponse;
import uk.co.danielturner.notesmanager.services.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock private AccountService accountService;
  @Mock private Principal principal;
  @InjectMocks private AccountController accountController;

  @Nested
  class Registration {

    @BeforeEach
    void setup() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void teardown() {
      RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void callsCreateFromService() {
      AccountRequest request = new AccountRequest();

      accountController.register(request);

      verify(accountService).create(request);
    }

    @Test
    void returnsCreatedResponseOnSuccess() {
      ResponseEntity<AccountResponse> response = accountController.register(new AccountRequest());

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void returnsLocationHeaderOnSuccess() {
      ResponseEntity<AccountResponse> response = accountController.register(new AccountRequest());

      assertThat(response.getHeaders().getLocation().toString()).endsWith("/v2/account");
    }
  }

  @Nested
  class Authentication {

    @Test
    void callsAuthenticateFromService() {
      AccountRequest request = new AccountRequest();

      accountController.authenticate(request);

      verify(accountService).authenticate(request);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      ResponseEntity<TokenResponse> response = accountController.authenticate(new AccountRequest());

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Nested
  class AccountDetails {

    @Test
    void callsGetDetailsFromService() {
      accountController.accountDetails(principal);

      verify(accountService).getDetails(principal);
    }

    @Test
    void returnsOkResponseOnSuccess() {
      ResponseEntity<AccountResponse> response = accountController.accountDetails(principal);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }
}