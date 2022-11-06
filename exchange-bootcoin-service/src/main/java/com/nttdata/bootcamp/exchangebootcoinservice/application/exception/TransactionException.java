package com.nttdata.bootcamp.exchangebootcoinservice.application.exception;

public class TransactionException  extends RuntimeException{
    public TransactionException(String message){
        super(message);
    }
}
