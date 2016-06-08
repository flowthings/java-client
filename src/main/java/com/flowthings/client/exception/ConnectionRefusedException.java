package com.flowthings.client.exception;

public class ConnectionRefusedException extends FlowthingsException {
  public ConnectionRefusedException() {
  }

  public ConnectionRefusedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ConnectionRefusedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConnectionRefusedException(String message) {
    super(message);
  }

  public ConnectionRefusedException(Throwable cause) {
    super(cause);
  }
}
