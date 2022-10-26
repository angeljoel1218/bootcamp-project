package com.nttdata.bootcamp.accountservice.application.exceptions;

public class FixedTermAccountException extends RuntimeException{
    public FixedTermAccountException(String message) {
        super(message);
    }
}
