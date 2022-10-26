package com.nttdata.bootcamp.accountservice.application.exceptions;

public class OperationAccountException extends RuntimeException{
    public OperationAccountException(String message){
        super(message);
    }
}
