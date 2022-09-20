package co.com.sofkoin.alpha.domain.values;

import co.com.sofka.domain.generic.ValueObject;
import org.apache.commons.validator.GenericValidator;

public class CashBalance implements ValueObject<Double> {
  private Double value;

  public CashBalance(Double value) {
    if(!GenericValidator.minValue(value, 0)) {
      throw new IllegalArgumentException("Invalid Value(Balance must be positive).");
    }

    this.value = value;
  }

  public Double value() {
    return value;
  }
}
