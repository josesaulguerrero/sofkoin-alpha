package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class Avatar implements ValueObject<String> {
  private final String value;

  public Avatar(String value) {
    if(!GenericValidator.isUrl(value)) {
      throw new IllegalArgumentException("Invalid URL for the avatar.");
    }

    this.value = value;
  }

  public String value() {
    return value;
  }
}
