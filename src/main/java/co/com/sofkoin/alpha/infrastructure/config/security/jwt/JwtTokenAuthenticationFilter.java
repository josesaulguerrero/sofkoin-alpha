package co.com.sofkoin.alpha.infrastructure.config.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

public class JwtTokenAuthenticationFilter implements WebFilter {
  public static final String TOKEN_PREFIX = "Bearer ";

  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = extractToken(exchange.getRequest());

    if(!StringUtils.hasText(token)) return chain.filter(exchange);

    Map<String, Object> claimsFromToken = JwtTokenProvider.getClaimsFromToken(token);

    if(claimsFromToken.isEmpty()) return chain.filter(exchange);

    boolean isValid = jwtTokenProvider.validateToken((Claims) claimsFromToken);

    if(!isValid) return chain.filter(exchange);

    Authentication authentication = jwtTokenProvider.getAuthentication((Claims) claimsFromToken);

    return
      chain.filter(exchange)
      .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
  }

  private String extractToken(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if(!StringUtils.hasText(bearerToken)) return null;

    if(!bearerToken.startsWith(TOKEN_PREFIX)) return null;

    return bearerToken.substring(7);
  }
}
