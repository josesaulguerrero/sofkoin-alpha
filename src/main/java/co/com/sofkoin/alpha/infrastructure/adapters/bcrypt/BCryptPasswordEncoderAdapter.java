package co.com.sofkoin.alpha.infrastructure.adapters.bcrypt;

import co.com.sofkoin.alpha.application.gateways.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public Boolean matches(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
