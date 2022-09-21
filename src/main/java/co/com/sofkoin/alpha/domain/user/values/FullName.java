package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

@EqualsAndHashCode
@ToString
public class FullName implements ValueObject<FullName.Values> {
    private final String name;
    private final String surname;

    public FullName(String name, String surname) {
        String FULL_NAME_REGEX = "[a-zA-Z]{3,}";
        if (!GenericValidator.matchRegexp(name, FULL_NAME_REGEX) && !GenericValidator.matchRegexp(surname, FULL_NAME_REGEX)) {
            throw new IllegalArgumentException("Invalid full name (Must contain at least three characters and no numbers).");
        }
        this.name = name;
        this.surname = surname;
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
