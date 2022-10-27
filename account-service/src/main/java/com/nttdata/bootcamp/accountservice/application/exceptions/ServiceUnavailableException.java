package com.nttdata.bootcamp.accountservice.application.exceptions;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
