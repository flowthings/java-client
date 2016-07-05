package com.flowthings.client.exception;

/**
 * Created by matt on 7/5/16.
 */
public class ConnectionLostException extends FlowthingsException {
  public ConnectionLostException() {
  }

  public ConnectionLostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ConnectionLostException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConnectionLostException(String message) {
    super(message);
  }

  public ConnectionLostException(Throwable cause) {
    super(cause);
  }
}
