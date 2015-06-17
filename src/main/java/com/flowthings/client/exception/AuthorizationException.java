package com.flowthings.client.exception;

public class AuthorizationException extends FlowthingsException {

  public AuthorizationException() {
  }

  public AuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AuthorizationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthorizationException(String message) {
    super(message);
  }

  public AuthorizationException(Throwable cause) {
    super(cause);
  }
}
