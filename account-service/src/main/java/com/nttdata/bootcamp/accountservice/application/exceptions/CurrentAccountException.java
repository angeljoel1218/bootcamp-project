package com.nttdata.bootcamp.accountservice.application.exceptions;

public class CurrentAccountException extends RuntimeException{
    public CurrentAccountException(String message) {
        super(message);
    }
}
