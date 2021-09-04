package uk.co.danielturner.notesmanager.models;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "accounts")
public class Account implements UserDetails {

  @Id
  @GeneratedValue
  UUID id;

  @Column(unique = true)
  String username;

  String password;
  GrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");
  boolean enabled = true;
  boolean accountNonExpired = true;
  boolean accountNonLocked = true;
  boolean credentialsNonExpired = true;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return Collections.singleton(role);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public static final class Builder {
    String username;
    String password;

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder withPassword(String password) {
      this.password = password;
      return this;
    }

    public Account build() {
      Account account = new Account();
      account.username = this.username;
      account.password = this.password;
      return account;
    }
  }
}
