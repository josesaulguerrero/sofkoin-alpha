package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.regex.Pattern;

public class Email implements ValueObject<String> {
  private static Pattern EMAIL_REGEX = Pattern.compile("\"\\\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}\\\\b\"");
  private String value;

  public Email(String value) {
    if(validateEmail(value)) {
      this.value = value;
    }
  }

  private static boolean validateEmail(String email) {
    if(!EMAIL_REGEX.matcher(email).find()) {
      throw new IllegalArgumentException("Invalid email(must follow this pattern 'a@b.xyz').");
    }
    return true;
  }

  public String value() {
    return value;
  }
}
