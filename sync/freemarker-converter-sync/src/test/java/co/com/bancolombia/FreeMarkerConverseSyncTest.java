package co.com.bancolombia;

import co.com.bancolombia.api.TemplateValidations;
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
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FreeMarkerConverseSyncTest {

    FreeMarkerConverseSync freeMarkerConverseSync;

    @BeforeEach
    void init(@Mock TemplateTransactionFreemarker templateTransactionFreemarker) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        freeMarkerConverseSync = new FreeMarkerConverseSync(templateTransactionFreemarker, objectMapper);
        FreeMarkerConfig freeMarkerConfig = new Config().freeMarkerConfig();
        Template templateJsonToXml = new Template(UUID.randomUUID().toString(), new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><field1>value1</field1>"), freeMarkerConfig);
        Template templateJsonToXmlError = new Template(UUID.randomUUID().toString(), new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" +
                "  <reason>401</reason>\n" +
                "  <domain>na</domain>\n" +
                "  <code>401</code>\n" +
                "  <error>error message</error>\n" +
                "</root>"), freeMarkerConfig);

        Template templateXmlToJson = new Template(UUID.randomUUID().toString(), new StringReader("{\"field1\":\"value1\"}"), freeMarkerConfig);
        Template templateXmlToJsonError = new Template(UUID.randomUUID().toString(), new StringReader("{\"reason\":\"401\",\"domain\":\"na\",\"code\":\"401\",\"error\":\"error message\"}"), freeMarkerConfig);
        TemplateValidations templateValidationsTrue = new TemplateValidations() {
            @Override
            public boolean isOkResponseXmlToObject(Map<?, ?> object) {
                return true;
            }

            @Override
            public boolean isOkResponseJsonToXml(String json) {
                return true;
            }
        };

        TemplateValidations templateValidationsFalse = new TemplateValidations() {
            @Override
            public boolean isOkResponseXmlToObject(Map<?, ?> object) {
                return false;
            }

            @Override
            public boolean isOkResponseJsonToXml(String json) {
                return false;
            }
        };
        TemplateTransactionFreemarker.ResourceTemplate resourceTemplateOut = TemplateTransactionFreemarker.ResourceTemplate.builder().templateJsonToXml(templateJsonToXml).templateJsonToXmlError(templateJsonToXmlError).templateXmlToJson(templateXmlToJson).templateXmlToJsonError(templateXmlToJsonError).templateValidations(templateValidationsTrue).build();
        TemplateTransactionFreemarker.ResourceTemplate resourceTemplateError = TemplateTransactionFreemarker.ResourceTemplate.builder().templateJsonToXml(templateJsonToXml).templateJsonToXmlError(templateJsonToXmlError).templateXmlToJson(templateXmlToJson).templateXmlToJsonError(templateXmlToJsonError).templateValidations(templateValidationsFalse).build();
        Mockito.lenient().when(templateTransactionFreemarker.get("1")).thenReturn(resourceTemplateOut);
        Mockito.lenient().when(templateTransactionFreemarker.get("2")).thenReturn(resourceTemplateError);
    }

    @Test
    void xmlToObjectTemplate() {
        assertEquals(TestClass.class, freeMarkerConverseSync.xmlToObject("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><status code=\"200\" message=\"TRANSACCION EXITOSA\" severity=\"info\"/><task><id>123</id><description>this is a description</description><title>title</title></task></root>", "1", TestClass.class).getClass());
    }


    @Test
    void xmlToObjectTemplateError() {
        assertThrows(ConverseException.class, () -> freeMarkerConverseSync.xmlToObject("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><status code=\"200\" message=\"TRANSACCION EXITOSA\" severity=\"info\"/><task><id>123</id><description>this is a description</description><title>title</title></task></root>", "2", TestClass.class));
    }

    @Test
    void xmlToObjectTemplateFail() {
        assertThrows(ConverseException.class, () -> freeMarkerConverseSync.xmlToObject("not is a xml", "1", TestClass.class));
    }

    @Test
    void jsonToXmlWithoutContext() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><field1>value1</field1>", freeMarkerConverseSync.jsonToXml("{\"field1\":\"value1\"}", "1"));

    }

    @Test
    void jsonToXmlWithContext() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><field1>value1</field1>", freeMarkerConverseSync.jsonToXml("{\"field1\":\"value1\"}", "1", new Object()));

    }
    @Test
    void jsonToXmlWithContextError() {
        assertThrows(ConverseException.class, () -> freeMarkerConverseSync.jsonToXml("{\"field1\":\"value1\"}", "2", new Object()));


    }

    @Test
    void jsonToXmlFail() {
        assertThrows(ConverseException.class, () -> freeMarkerConverseSync.jsonToXml("not is a json", "1"));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class TestClass {
        private String field1;
    }
}