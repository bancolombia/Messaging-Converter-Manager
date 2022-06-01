package co.com.bancolombia.api.handlerexception;


import co.com.bancolombia.api.handlerexception.model.error.ErrorResponse;
import co.com.bancolombia.api.handlerexception.model.error.ErrorResponseData;
import co.com.bancolombia.model.exception.TechnicalException;
import co.com.bancolombia.model.exception.TechnicalExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Arrays;

@Order(-2)
@Component
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {


    @Autowired
    public ExceptionHandler(ErrorAttributes errorAttributes,
                            ApplicationContext applicationContext,
                            ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::buildErrorResponse);
    }

    private Mono<ServerResponse> buildErrorResponse(final ServerRequest request) {
        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(TechnicalException.class, ex -> this.errorBuilder(ex, request.path()))
                .onErrorResume(throwable -> this.errorBuilder(throwable, request.path()))
                .cast(Tuple2.class)
                .flatMap(tuple -> this.buildResponse((ErrorResponseData) tuple.getT1(), (HttpStatus) tuple.getT2()));
    }


    public Mono<Tuple2<ErrorResponseData, HttpStatus>> errorBuilder(TechnicalException responseException, String path) {
        return Mono.just(ErrorResponseData.builder().errors(Arrays.asList(ErrorResponse.builder()
                        .code(responseException.getErrorMessage().getCode())
                        .domain(path)
                        .reason(responseException.getErrorMessage().getMessage())
                        .message(responseException.getErrorMessage().getMessage())
                        .build())).build())
                .zipWith(Mono.just(HttpStatus
                        .resolve(Integer.parseInt(responseException.getErrorMessage().getCode()))));
    }

    public Mono<Tuple2<ErrorResponseData, HttpStatus>> errorBuilder(Throwable throwable, String path) {
        return Mono.just(ErrorResponseData.builder().errors(Arrays.asList(ErrorResponse.builder()
                        .code(TechnicalExceptionEnum.UNKNOWN_ERROR.getCode())
                        .domain(path)
                        .reason(TechnicalExceptionEnum.UNKNOWN_ERROR.getMessage())
                        .message(TechnicalExceptionEnum.UNKNOWN_ERROR.getMessage())
                        .build())).build())
                .zipWith(Mono.just(HttpStatus
                        .resolve(Integer.parseInt((TechnicalExceptionEnum.UNKNOWN_ERROR.getCode())))));
    }

    public Mono<ServerResponse> buildResponse(ErrorResponseData error, HttpStatus httpStatus) {
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(error), ErrorResponseData.class);
    }
}
