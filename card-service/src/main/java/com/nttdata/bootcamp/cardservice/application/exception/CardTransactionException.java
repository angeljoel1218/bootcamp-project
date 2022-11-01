package com.nttdata.bootcamp.cardservice.application.exception;

public class CardTransactionException extends RuntimeException{
    public CardTransactionException(String message){
        super(message);
    }
}
