package org.evilkitten.gitboard.application.services.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.evilkitten.gitboard.application.exception.GitboardException;

public class JacksonJsonEncoder implements JsonEncoder {
    private final ObjectMapper objectMapper;

    public JacksonJsonEncoder() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        this.objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        this.objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    @Override
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new GitboardException("Unable to serialize object: " + object, e);
        }
    }
}
