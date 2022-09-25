package co.com.sofkoin.alpha.infrastructure.config.security;

import co.com.sofkoin.alpha.application.gateways.DomainEventRepository;
import co.com.sofkoin.alpha.domain.common.values.identities.UserID;
import co.com.sofkoin.alpha.domain.user.events.UserSignedUp;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JWTAuthenticationFilter;
import co.com.sofkoin.alpha.infrastructure.config.security.jwt.JWTProvider;
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
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain getWebFilterChain(
            ServerHttpSecurity http,
            JWTProvider JWTProvider,
            CorsConfigurationSource corsConfigurationSource
    ) {
        return http
                .cors().configurationSource(corsConfigurationSource).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers("/auth/login", "/auth/signup").permitAll()
                .pathMatchers("/auth/logout", "/transaction/**", "/message/**", "/market/**").authenticated()
                .and()
                .addFilterAt(new JWTAuthenticationFilter(JWTProvider), SecurityWebFiltersOrder.HTTP_BASIC)
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
                                .findUserDomainEventsByEmail(email)
                                .collectList()
                                .map(events -> {
                                    var user = co.com.sofkoin.alpha.domain.user.entities.root.User.from(
                                            new UserID(((UserSignedUp) events.get(0)).getUserId()), events
                                    );
                                    return
                                            User
                                                    .withUsername(user.email().value())
                                                    .password(user.password().value())
                                                    .authorities(Collections.emptyList())
                                                    .build();
                                });
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
