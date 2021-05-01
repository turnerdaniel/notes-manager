package uk.co.danielturner.notesmanager.filters;

import java.io.IOException;
import java.util.StringJoiner;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class NotesLoggingFilter extends OncePerRequestFilter {

  private final static Logger LOGGER = LoggerFactory.getLogger(NotesLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    long requestTime = 0L;
    try {
      requestTime = System.currentTimeMillis();
      filterChain.doFilter(request, response);

    } finally {
      long responseTime = System.currentTimeMillis() - requestTime;
      StringJoiner statistics = new StringJoiner(" ")
          .add(request.getMethod())
          .add(request.getRequestURI())
          .add("from")
          .add(request.getRemoteAddr())
          .add("|")
          .add(String.valueOf(response.getStatus()))
          .add(HttpStatus.valueOf(response.getStatus()).getReasonPhrase())
          .add("took")
          .add(responseTime + "ms");

      LOGGER.info(statistics.toString());
    }
  }
}
