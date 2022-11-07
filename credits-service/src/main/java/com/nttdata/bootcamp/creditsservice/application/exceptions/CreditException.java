package com.nttdata.bootcamp.creditsservice.application.exceptions;

/**
 *
 * @since 2022
 */
public class CreditException extends RuntimeException{
    public CreditException(String message) {
        super(message);
    }
}
