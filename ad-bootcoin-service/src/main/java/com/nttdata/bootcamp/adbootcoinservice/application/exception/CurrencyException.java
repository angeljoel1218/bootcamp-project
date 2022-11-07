package com.nttdata.bootcamp.adbootcoinservice.application.exception;



/**
 *
 * @since 2022
 */
public class CurrencyException extends RuntimeException{
    public CurrencyException(String message){
        super(message);
    }
}
