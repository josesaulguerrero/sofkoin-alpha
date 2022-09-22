package co.com.sofkoin.alpha.infrastructure.config.security;

import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JwtTokenAuthenticationFilter;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfiguration {
  @Bean
  public SecurityWebFilterChain getWebFilterChain(
          ServerHttpSecurity http,
          JwtTokenProvider jwtTokenProvider
  ) {
    return http
            .cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authorizeExchange()
            .and()
            .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
            .build();
  }

  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager(
          PasswordEncoder passwordEncoder,
          ReactiveUserDetailsService reactiveUserDetailsService) {

    var repositoryReactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
    repositoryReactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);

    return repositoryReactiveAuthenticationManager;
  }

  @Bean
  public ReactiveUserDetailsService userDetailsService(DomainEventRepository userRepository) {
    return
      email ->
        userRepository
          .findDomainEventsByEmail(email)
          .collectList()
          .map(events -> {

            var user = co.com.sofkoin.alpha.domain
                    .user.entities.root.User.from(
                            new UserID(events.get(0).aggregateRootId()),
                            events);

            return
                User
                .withUsername(
                    user.fullName().value().name() + " "
                    + user.fullName().value().surname()
                )
                .password(user.password().value())
                .build();
          });
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
