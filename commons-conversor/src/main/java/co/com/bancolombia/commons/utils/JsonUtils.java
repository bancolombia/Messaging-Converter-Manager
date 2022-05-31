package co.com.bancolombia.commons.utils;

import co.com.bancolombia.exceptions.ConverseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;


@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public <T> T stringToType(String msg, Class<T> cls) throws ConverseException {
        try {
            return OBJECT_MAPPER.readValue(msg, cls);
        }catch (IOException e){
            throw new ConverseException(e,"JsonUtils.stringToType");
        }
    }


}