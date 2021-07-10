package uk.co.danielturner.notesmanager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.util.Date;
import org.springframework.stereotype.Service;
import uk.co.danielturner.notesmanager.models.Account;

@Service
public class JwtHelper {
  private static final String SECRET = "something";
  private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

  public String getSubject(String token) {
    return JWT.decode(token).getSubject();
  }

  public String generateToken(Account account) {
    return createJwt(account.getUsername());
  }

  public Boolean verifyToken(String token) {
    JWTVerifier verifier = JWT.require(algorithm).build();
    try {
      verifier.verify(token);
    } catch (JWTDecodeException e) {
      throw new RuntimeException("", e);
    }
    return true;
  }

  private String createJwt(String subject) {
    return JWT.create()
        .withSubject(subject)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10 ))
        .sign(algorithm);
  }
}
