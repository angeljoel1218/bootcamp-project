package com.nttdata.bootcamp.productservice.application.exception;

import com.nttdata.bootcamp.productservice.model.dto.ApiError;
import com.nttdata.bootcamp.productservice.model.dto.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;



/**
 * Some javadoc.
 *
 * @since 2022
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiError>> handleGlobalException(Exception ex){
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());
        ApiError apiError = new ApiError();
        apiError.setCode(ResponseCode.INVALID_DATA);
        apiError.setMessage(ex.getMessage());
        apiError.setErrors(errors);
        return Mono.just(ResponseEntity.internalServerError().body(apiError));
    }
}
