package uk.co.danielturner.notesmanager.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

  @Autowired
  JwtHelper jwtHelper;

  @Autowired
  AccountService accountService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
      String jwt = authorisationHeader.split(" ")[1];
      if (jwtHelper.verifyToken(jwt)) {
        String username = jwtHelper.getSubject(jwt);
        Account account = accountService.loadUserByUsername(username);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
            account, null, account.getAuthorities()));
      }
    }

    filterChain.doFilter(request, response);
  }
}
