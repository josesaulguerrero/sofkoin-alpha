package co.com.sofkoin.alpha.domain.user.values;

import co.com.sofka.domain.generic.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@EqualsAndHashCode
public class Timestamp implements ValueObject<LocalDateTime> {
    private final LocalDateTime value;

    public Timestamp() {
        this.value = LocalDateTime.now();
    }

    public Timestamp(LocalDateTime value) {
        this.value = value;
    }

    public static Timestamp from(String sequence) {
        String dateFormat = "MM/dd/yyyy HH:mm:ss";
        if (!GenericValidator.isDate(sequence, dateFormat, true)) {
            throw new IllegalArgumentException("Invalid char sequence; The sequence must follow this pattern: month/day/year hours:minutes:seconds.");
        }
        return new Timestamp(
                LocalDateTime.parse(sequence, DateTimeFormatter.ofPattern(dateFormat))
        );
    }

    @Override
    public LocalDateTime value() {
        return this.value;
    }
}
