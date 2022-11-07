package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.exception;

/**
 *
 * @since 2022
 */

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
