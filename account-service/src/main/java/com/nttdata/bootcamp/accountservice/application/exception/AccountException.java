package com.nttdata.bootcamp.accountservice.application.exception;

public class AccountException extends RuntimeException{
    public AccountException(String message){
        super(message);
    }
}
