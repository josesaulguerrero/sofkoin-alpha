package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.regex.Pattern;

@EqualsAndHashCode
@ToString
public class FullName implements ValueObject<FullName.Values> {
  private static final Pattern FULL_NAME_REGEX = Pattern.compile("[a-zA-Z]{3,}");
  private String name;
  private String surname;

  public FullName(String name, String surname) {
    if(validateFullName(name, surname)) {
      this.name = name;
      this.surname = surname;
    }
  }

  private static boolean validateFullName(String name, String surname) {
    if(!FULL_NAME_REGEX.matcher(name).find() && !FULL_NAME_REGEX.matcher(surname).find()) {
      throw new IllegalArgumentException("Invalid full name (Must contain at least three characters and no numbers).");
    }
    return true;
  }

  public interface Values {
    String name();
    String surname();
  }

  @Override
  public Values value() {
    return new Values() {
      @Override
      public String name() {
        return name;
      }

      @Override
      public String surname() {
        return surname;
      }
    };
  }
}
