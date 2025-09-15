package org.example.star.utill.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class JsonConverter {
    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object object) {
        try {
           return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось преобразовать в JSON",e);
        }
    }

    public <T> T fromJson(String json,Class<T> type) {
        try {
            return objectMapper.readValue(json,type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось преобразовать в объект",e);
        }
    }

    public String toJsonList(List<?>list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось преобразовать в список JSON",e);
        }
    }

    public <T> List<T> fromJsonList(String json,Class<T> type)  {
       try {
           return objectMapper.readValue(json,
                   objectMapper.getTypeFactory().constructCollectionType(List.class, type));
       } catch (JsonProcessingException e) {
           throw new RuntimeException("Не удалось преобразовать в список объектов",e);
       }
    }
}
