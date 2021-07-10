package uk.co.danielturner.notesmanager.controllers;

import java.net.URI;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.Token;
import uk.co.danielturner.notesmanager.services.AccountService;

@RestController
@RequestMapping("/v2")
public class AccountController {

  @Autowired
  AccountService accountService;

  @PostMapping("/register")
  public ResponseEntity<Account> register(@RequestBody Account account) {
    final Account createdAccount = accountService.create(account);
    final URI uri = MvcUriComponentsBuilder
        .fromController(AccountController.class)
        .pathSegment("account")
        .build()
        .toUri();
    return ResponseEntity.created(uri).body(createdAccount);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<Token> authenticate(@RequestBody Account account) {
    return ResponseEntity.ok(accountService.authenticate(account));
  }

  @GetMapping("/account")
  public ResponseEntity<Account> accountDetails(Principal principal) {
    return ResponseEntity.ok(accountService.getDetails(principal));
  }
}
