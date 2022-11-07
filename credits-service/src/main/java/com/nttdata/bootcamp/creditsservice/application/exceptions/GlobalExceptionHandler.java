package com.nttdata.bootcamp.creditsservice.application.exceptions;

import com.nttdata.bootcamp.creditsservice.model.dto.ApiError;
import com.nttdata.bootcamp.creditsservice.model.dto.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
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
