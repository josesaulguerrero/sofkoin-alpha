package co.com.sofkoin.alpha.infrastructure.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component
@Data
@Slf4j
public class JWTProvider {
  private static final String SECRET_KEY = "You°are°few°clicks°away°to°be°fooled";
  private static final Long VALID_TIME = 3600000L;
  private static final SecretKey secretKey =
    Keys.hmacShaKeyFor(
      Base64.getEncoder().encodeToString(SECRET_KEY.getBytes(StandardCharsets.UTF_8)).getBytes()
    );

  public String createJwtToken(Authentication authentication) {
    String name = authentication.getName();

    Claims claims = Jwts.claims().setSubject(name);

    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + VALID_TIME);

    return
      Jwts
        .builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Authentication getAuthentication(Claims claims) {
    User user = new User(claims.getSubject(), "", Collections.emptyList());

    return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
  }


  public Boolean validateToken(Claims claims) {
    if (claims.getExpiration().before(new Date())) {
      log.info("[Invalid token]: Expiration time was " + claims.getExpiration().toString());
      return false;
    }

    log.info("[Token is ok]: Expiration time is " + claims.getExpiration().toString());
    return true;
  }


  public static Map<String, Object> getClaimsFromToken(String token) {
    try {

      return Jwts.parserBuilder()
              .setSigningKey(secretKey)
              .build()
              .parseClaimsJws(token)
              .getBody();

    } catch(JwtException | IllegalArgumentException e){

      log.info("[Invalid token]: "+ e.getMessage());
      return Collections.emptyMap();
    }
  }

}
