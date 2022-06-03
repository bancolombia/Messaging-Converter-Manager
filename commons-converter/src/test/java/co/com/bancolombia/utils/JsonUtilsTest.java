package co.com.bancolombia.utils;

import co.com.bancolombia.commons.utils.JsonUtils;
import co.com.bancolombia.exceptions.ConverseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonUtilsTest {

    @Test
    void stringToTypeTest() {
        assertEquals(TestClass.class, JsonUtils.stringToType("{\"testFiled\":\"test\"}", TestClass.class).getClass());
    }


    @Test
    void stringToTypeFail() {
        assertThrows(ConverseException.class, () -> JsonUtils.stringToType("Hola", Integer.class));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class TestClass {
        private String testFiled;
    }

}
