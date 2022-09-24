package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString
@EqualsAndHashCode
public class Password implements ValueObject<String> {
    private final String value;

    public Password(String value) {

        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(value);

        if (!matcher.find() && !GenericValidator.isEmail(value) ) {
            throw new IllegalArgumentException("Invalid Password (At least eight characters, one number, " +
                    "one lowercase and one uppercase).");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
