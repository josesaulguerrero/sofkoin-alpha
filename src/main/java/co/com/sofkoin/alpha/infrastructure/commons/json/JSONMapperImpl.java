package co.com.sofkoin.alpha.infrastructure.commons.json;

import co.com.sofkoin.alpha.infrastructure.exceptions.json.JSONDeserializationException;
import co.com.sofkoin.alpha.infrastructure.exceptions.json.JSONSerializationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class JSONMapperImpl implements JSONMapper {
    private final ObjectMapper mapper;

    public JSONMapperImpl() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String writeToJson(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JSONSerializationException(
                    String.format("Couldn't parse %s to a JSON string.", object.toString())
            );
        }
    }

    @Override
    public Object readFromJson(String json, Class<?> clazz) {
        try {
            return this.mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new JSONDeserializationException(
                    String.format("Couldn't map %s to a valid instance of %s.", json, clazz.getName())
            );
        }
    }
}
