package co.com.sofkoin.alpha.infrastructure.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOriginPatterns("https://sofkoin.web.app", "http://localhost:[*]")
                .allowedMethods("POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

}
