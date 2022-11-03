package com.nttdata.bootcamp.wallettransactionservice.application.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
