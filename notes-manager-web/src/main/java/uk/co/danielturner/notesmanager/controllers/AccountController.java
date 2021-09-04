package uk.co.danielturner.notesmanager.controllers;

import java.net.URI;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import uk.co.danielturner.notesmanager.models.dtos.TokenResponse;
import uk.co.danielturner.notesmanager.models.dtos.AccountRequest;
import uk.co.danielturner.notesmanager.models.dtos.AccountResponse;
import uk.co.danielturner.notesmanager.services.AccountService;

@RestController
@RequestMapping("/v2")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/register")
  public ResponseEntity<AccountResponse> register(@RequestBody AccountRequest request) {
    final AccountResponse response = accountService.create(request);
    final URI uri = MvcUriComponentsBuilder
        .fromController(AccountController.class)
        .pathSegment("account")
        .build()
        .toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<TokenResponse> authenticate(@RequestBody AccountRequest request) {
    return ResponseEntity.ok(accountService.authenticate(request));
  }

  @GetMapping("/account")
  public ResponseEntity<AccountResponse> accountDetails(Principal principal) {
    return ResponseEntity.ok(accountService.getDetails(principal));
  }
}
