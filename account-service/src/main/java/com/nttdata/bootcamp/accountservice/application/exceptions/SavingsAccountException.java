package com.nttdata.bootcamp.accountservice.application.exceptions;

public class SavingsAccountException extends RuntimeException{
    public SavingsAccountException(String message){
        super(message);
    }
}
