package com.nttdata.bootcamp.cardservice.application.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
