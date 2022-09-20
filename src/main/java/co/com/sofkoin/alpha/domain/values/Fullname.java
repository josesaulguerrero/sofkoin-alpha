package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.regex.Pattern;

@EqualsAndHashCode
@ToString
public class Fullname implements ValueObject<Fullname.Values> {
  private static Pattern FULLNAME_REGEX = Pattern.compile("[a-zA-Z]{3,}");
  private String name;
  private String surname;

  public Fullname(String name, String surname) {
    if(validateFullName(name, surname)) {
      this.name = name;
      this.surname = surname;
    }
  }

  private static boolean validateFullName(String name, String surname) {
    if(!FULLNAME_REGEX.matcher(name).find() && !FULLNAME_REGEX.matcher(surname).find()) {
      throw new IllegalArgumentException("Invalid full name(At least three characters and no numbers).");
    }
    return true;
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

  public interface Values {
    String name();
    String surname();
  }


}
