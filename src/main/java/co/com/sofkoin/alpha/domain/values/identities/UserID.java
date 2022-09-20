package co.com.sofkoin.alpha.domain.values.identities;

import co.com.sofka.domain.generic.Identity;

public class UserID extends Identity {
  private final String value;

  public UserID() {
    this.value = super.generateUUID().toString();
  }

  public UserID(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
