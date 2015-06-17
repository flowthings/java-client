package com.flowthings.client.exception;

public class FlowthingsException extends Exception {

  public FlowthingsException() {
    super();
  }

  public FlowthingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public FlowthingsException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlowthingsException(String message) {
    super(message);
  }

  public FlowthingsException(Throwable cause) {
    super(cause);
  }

}
