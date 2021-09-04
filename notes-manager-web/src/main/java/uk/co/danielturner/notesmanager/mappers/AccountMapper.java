package uk.co.danielturner.notesmanager.mappers;

import org.springframework.stereotype.Component;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.dtos.AccountResponse;

@Component
public class AccountMapper {

  public AccountResponse convertToAccountResponse(Account account){
    if (account == null) {
      return null;
    }

    AccountResponse response = new AccountResponse();
    response.setId(account.getId());
    response.setUsername(account.getUsername());
    return response;
  }
}
