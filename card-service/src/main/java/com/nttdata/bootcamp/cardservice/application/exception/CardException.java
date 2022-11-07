package com.nttdata.bootcamp.cardservice.application.exception;

/**
 *
 * @since 2022
 */
public class CardException extends RuntimeException{
    public CardException(String message){
        super(message);
    }
}
