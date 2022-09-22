package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@ToString
@EqualsAndHashCode
public class Password implements ValueObject<String> {
    private final String value;

    public Password(String value) {
        String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        System.out.println("value = " + value);
        if (!GenericValidator.isEmail(value) && !GenericValidator.matchRegexp(value, PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Invalid Password (At least eight characters, one number, one lowercase and one uppercase).");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
