package com.nttdata.bootcamp.cardservice.application.exception;

/**
 *
 * @since 2022
 */
public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
