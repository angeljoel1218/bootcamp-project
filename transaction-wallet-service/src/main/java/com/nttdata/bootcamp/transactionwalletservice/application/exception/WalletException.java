package com.nttdata.bootcamp.transactionwalletservice.application.exception;

public class WalletException extends RuntimeException{
    public WalletException(String message){
        super(message);
    }
}
