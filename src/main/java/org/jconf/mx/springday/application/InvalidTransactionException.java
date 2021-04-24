package org.jconf.mx.springday.application;

public class InvalidTransactionException extends RuntimeException {

  public InvalidTransactionException(String error) {
    super(error);
  }
}
