package co.com.bancolombia.models;

import co.com.bancolombia.api.TemplateValidations;
import lombok.*;

import java.util.TreeMap;

@Builder
@Data
@Getter
@NoArgsConstructor
public class TemplateTransaction extends TreeMap<String, TemplateTransaction.ResourceTemplate> {

    @Builder(toBuilder = true)
    @Data
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceTemplate {
        private String transactionName;
        private String channel;
        private String transaction;
        /**
         * @param templateIn.
         * @deprecated  reason this parameter is renamed <br/>
         *               {will be removed in next version} <br/>
         *               use <b>templateJsonToXml</b> instead.
         */
        @Deprecated
        private String templateIn;
        private String templateJsonToXml;
        private String templateJsonToXmlError;

        /**
         * @param templateOut.
         * @deprecated  reason this parameter is renamed <br/>
         *               {will be removed in next version} <br/>
         *               use <b>templateXmlToJson</b> instead.
         */
        @Deprecated
        private String templateOut;
        private String templateXmlToObject;
        /**
         * @param templateError.
         * @deprecated  reason this parameter is renamed <br/>
         *               {will be removed in next version} <br/>
         *               use <b>templateXmlToJsonError</b> instead.
         */
        @Deprecated
        private String templateError;
        private String templateXmlToObjectError;
        private TemplateValidations templateValidations;
    }
}
