package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.regex.Pattern;

@EqualsAndHashCode
@ToString
public class Password implements ValueObject<String> {
  private static Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d#$@!%&*?]{8,30}$");
  private String value;

  public Password(String value) {
    if(validatePassword(value)) {
      this.value = value;
    }
  }

  private static boolean validatePassword(String password) {
    if(!PASSWORD_REGEX.matcher(password).find()) {
      throw new IllegalArgumentException("Invalid Password(At least eight characters, one number, one lowercase and one uppercase).");
    }

    return true;
  }

  public String value() {
    return value;
  }
}
