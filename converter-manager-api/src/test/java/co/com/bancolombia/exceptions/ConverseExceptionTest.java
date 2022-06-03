package co.com.bancolombia.exceptions;


import co.com.bancolombia.models.ErrorConverse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverseExceptionTest {

    @Test
    void generateExceptionWithErrorConverse() {
        ConverseException ex = new ConverseException(ErrorConverse.builder().reason("My error").code("").error("").domain("").build());
        assertEquals("My error", ex.getMessage());
    }

    @Test
    void generateExceptionWithMessage() {
        Throwable cause = new RuntimeException();
        ConverseException ex = new ConverseException(cause, "My error");
        assertEquals("My error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

}
