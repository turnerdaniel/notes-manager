package uk.co.danielturner.notesmanager.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.co.danielturner.notesmanager.models.Account;
import uk.co.danielturner.notesmanager.services.AccountService;
import uk.co.danielturner.notesmanager.utils.JwtHelper;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

  @Autowired private JwtHelper jwtHelper;
  @Autowired private AccountService accountService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
      String token = authorisationHeader.split(" ")[1];
      UUID id = retrieveAccountIdFromJwt(token);

      if (id != null) {
        setSecurityContext(id);
      }
    }

    filterChain.doFilter(request, response);
  }

  private UUID retrieveAccountIdFromJwt(String token) {
    try {
      return UUID.fromString(jwtHelper.getSubject(token));
    } catch (JWTVerificationException e) {
      LOGGER.warn("Authorisation token could not be verified", e);
      return null;
    }
  }

  private void setSecurityContext(UUID id) {
    Account account = accountService.loadUserById(id);
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        account, null, account.getAuthorities()));
  }
}
