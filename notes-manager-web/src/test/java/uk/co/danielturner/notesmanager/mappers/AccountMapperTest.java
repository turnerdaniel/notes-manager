package uk.co.danielturner.notesmanager.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.models.dtos.AccountResponse;

class AccountMapperTest {

  private final AccountMapper accountMapper = new AccountMapper();

  @Test
  void convertAccountToResponseCorrectly() {
    final String username = "example@company.com";
    final UUID id = UUID.randomUUID();
    Account account = new Account();
    account.setUsername(username);
    account.setId(id);

    AccountResponse response = accountMapper.convertToAccountResponse(account);

    assertThat(response.getId()).isEqualTo(id);
    assertThat(response.getUsername()).isEqualTo(username);
  }

  @Test
  void returnsNullResponseWhenNoteIsNull() {
    assertThat(accountMapper.convertToAccountResponse(null)).isNull();
  }
}
