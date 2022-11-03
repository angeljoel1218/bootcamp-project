package com.nttdata.bootcamp.wallettransactionservice.application.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message){
        super(message);
    }
}
