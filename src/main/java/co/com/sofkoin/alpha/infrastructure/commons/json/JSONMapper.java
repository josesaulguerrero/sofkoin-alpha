package co.com.sofkoin.alpha.infrastructure.commons.json;

public interface JSONMapper {
    String writeToJson(Object object);
    Object readFromJson(String json, Class<?> clazz);
}
