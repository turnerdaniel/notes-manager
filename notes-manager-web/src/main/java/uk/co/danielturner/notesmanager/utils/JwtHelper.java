package uk.co.danielturner.notesmanager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.danielturner.notesmanager.models.Account;

@Service
public class JwtHelper {
  private static final String SECRET = "something";

  private final Algorithm signature = Algorithm.HMAC256(SECRET);
  private final String issuer = "uk.co.danielturner.notesmanager";

  public String getSubject(String token) {
    JWTVerifier verifier = JWT
        .require(signature)
        .withIssuer(issuer)
        .build();
    return verifier.verify(token).getSubject();
  }

  public String generateToken(Account account) {
    return JWT
        .create()
        .withSubject(account.getId().toString())
        .withIssuer(issuer)
        .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(6)))
        .sign(signature);
  }
}
