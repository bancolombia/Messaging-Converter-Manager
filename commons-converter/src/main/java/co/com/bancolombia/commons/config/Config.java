package co.com.bancolombia.commons.config;

import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Config {
    @Bean
    public FreeMarkerConfig freeMarkerConfig() {
        return new FreeMarkerConfig(Configuration.VERSION_2_3_29);
    }


}
