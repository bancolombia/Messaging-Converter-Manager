package co.com.bancolombia.commons.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class FreeMarkerConfig extends Configuration {

    public FreeMarkerConfig(Version version) {
        super(version);
        setDefaultEncoding("UTF-8");
        setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        setLogTemplateExceptions(false);
        setWrapUncheckedExceptions(true);
        setFallbackOnNullLoopVariable(false);
        setWrapUncheckedExceptions(true);

    }


}