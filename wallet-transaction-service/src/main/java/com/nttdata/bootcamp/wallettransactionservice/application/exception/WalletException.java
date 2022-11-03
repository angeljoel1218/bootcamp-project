package com.nttdata.bootcamp.wallettransactionservice.application.exception;

public class WalletException extends RuntimeException{
    public WalletException(String message){
        super(message);
    }
}
