package com.nttdata.bootcamp.transactionwalletservice.application.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
