package co.com.sofkoin.alpha.domain.values.identities;

import co.com.sofka.domain.generic.Identity;

public class MarketID extends Identity {

  private final String value;

  public MarketID() {
    this.value = super.generateUUID().toString();
  }

  public MarketID(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
