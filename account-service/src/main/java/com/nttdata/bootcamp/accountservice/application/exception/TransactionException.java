package com.nttdata.bootcamp.accountservice.application.exception;

public class TransactionException extends RuntimeException {
  public TransactionException(String message) {
    super(message);
  }
}
