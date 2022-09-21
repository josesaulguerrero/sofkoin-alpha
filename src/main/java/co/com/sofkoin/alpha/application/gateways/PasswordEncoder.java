package co.com.sofkoin.alpha.application.gateways;

public interface PasswordEncoder {
    String encode(String rawPassword);
    Boolean matches(String rawPassword, String encodedPassword);
}
