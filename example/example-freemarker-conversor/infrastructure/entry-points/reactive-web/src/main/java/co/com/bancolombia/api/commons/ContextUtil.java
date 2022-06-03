package co.com.bancolombia.api.commons;

import co.com.bancolombia.model.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class ContextUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Context buildContext(Map<String, String> headers){
        return Context.builder()
                .id(Optional.ofNullable(headers.get("id")).orElse(""))
                .dateTime(LocalDateTime.parse(Optional.ofNullable(headers.get("timestamp"))
                        .orElse(""),formatter))
                .templateCode(Optional.ofNullable(headers.get("template-code")).orElse(""))
                .build();
    }

}
