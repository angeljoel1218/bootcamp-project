package com.nttdata.bootcamp.adbootcoinservice.application.exception;

public class CurrencyException extends RuntimeException{
    public CurrencyException(String message){
        super(message);
    }
}
