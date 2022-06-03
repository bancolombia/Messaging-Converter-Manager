package co.com.bancolombia;

import co.com.bancolombia.commons.config.Config;
import co.com.bancolombia.commons.config.FreeMarkerConfig;
import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.models.TemplateTransactionFreemarker;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FreeMarkerConverseAsyncTest {

    FreeMarkerConverseAsync freeMarkerConverseAsync;

    @BeforeEach
    void init(@Mock TemplateTransactionFreemarker templateTransactionFreemarker) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        FreeMarkerConverseSync freeMarkerConverseSync = new FreeMarkerConverseSync(templateTransactionFreemarker, objectMapper);
        freeMarkerConverseAsync = new FreeMarkerConverseAsync(freeMarkerConverseSync);
        FreeMarkerConfig freeMarkerConfig = new Config().freeMarkerConfig();
        Template templateIn = new Template(UUID.randomUUID().toString(), new StringReader("template in"), freeMarkerConfig);
        Template templateOut = new Template(UUID.randomUUID().toString(), new StringReader("{\"field1\":\"value1\"}"), freeMarkerConfig);
        Template templateError = new Template(UUID.randomUUID().toString(), new StringReader("{\"reason\":\"401\",\"domain\":\"na\",\"code\":\"401\",\"error \":\"error message\"}"), freeMarkerConfig);
        TemplateTransactionFreemarker.ResourceTemplate resourceTemplateOut = TemplateTransactionFreemarker.ResourceTemplate.builder().templateIn(templateIn).templateOut(templateOut).templateError(templateError).templateValidations(response -> true).build();
        TemplateTransactionFreemarker.ResourceTemplate resourceTemplateError = TemplateTransactionFreemarker.ResourceTemplate.builder().templateIn(templateIn).templateOut(templateOut).templateError(templateError).templateValidations(response -> false).build();
        Mockito.lenient().when(templateTransactionFreemarker.get("1")).thenReturn(resourceTemplateOut);
        Mockito.lenient().when(templateTransactionFreemarker.get("2")).thenReturn(resourceTemplateError);
    }

    @Test
    public void xmlToObjectTemplateOut() {
        assertEquals(TestClass.class, freeMarkerConverseAsync.xmlToObject("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><status code=\"200\" message=\"TRANSACCION EXITOSA\" severity=\"info\"/><task><id>123</id><description>this is a description</description><title>title</title></task></root>", "1", TestClass.class).block().getClass());
    }





    @Test
    public void jsonToXmlWithoutContext() {
        assertEquals("template in", freeMarkerConverseAsync.jsonToXml("{\"field1\":\"value1\"}", "1").block());

    }

    @Test
    public void jsonToXmlWithContext() {
        assertEquals("template in", freeMarkerConverseAsync.jsonToXml("{\"field1\":\"value1\"}", "1", new Object()).block());

    }



    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class TestClass {
        private String field1;
    }
}