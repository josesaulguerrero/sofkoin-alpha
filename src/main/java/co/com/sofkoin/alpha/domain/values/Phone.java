package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.regex.Pattern;

@EqualsAndHashCode
@ToString
public class Phone implements ValueObject<String> {

  private static Pattern PHONE_REGEX = Pattern.compile("^\\d{10}$");
  private String value;

  public Phone(String value) {
    if(validatePhone(value)) {
      this.value = value;
    }
  }

  public boolean validatePhone(String number) {
    if(!PHONE_REGEX.matcher(number).find()) {
      throw new IllegalArgumentException("Invalid Phone(Exactly 10 numbers).");
    }

    return true;
  }

  public String value() {
    return value;
  }
}
