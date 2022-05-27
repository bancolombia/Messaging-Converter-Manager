package co.com.bancolombia.commons.utils;

import co.com.bancolombia.model.exception.TechnicalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;

import static co.com.bancolombia.model.exception.TechnicalExceptionEnum.TECHNICAL_JSON_ERROR;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public <T> T stringToType(String msg, Class<T> cls){
        try {
            return OBJECT_MAPPER.readValue(msg, cls);
        }catch (IOException exception){
            throw new TechnicalException(exception, TECHNICAL_JSON_ERROR);
        }
    }


}