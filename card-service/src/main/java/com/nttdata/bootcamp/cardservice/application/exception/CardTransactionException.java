package com.nttdata.bootcamp.cardservice.application.exception;

/**
 *
 * @since 2022
 */
public class CardTransactionException extends RuntimeException{
    public CardTransactionException(String message){
        super(message);
    }
}
