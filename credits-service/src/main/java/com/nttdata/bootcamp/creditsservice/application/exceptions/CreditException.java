package com.nttdata.bootcamp.creditsservice.application.exceptions;

public class CreditException extends RuntimeException{
    public CreditException(String message) {
        super(message);
    }
}
