package com.flowthings.client.exception;

/**
 * Created by matt on 7/5/16.
 */
public class AsyncException extends FlowthingsException {
  public AsyncException() {
  }

  public AsyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AsyncException(String message, Throwable cause) {
    super(message, cause);
  }

  public AsyncException(String message) {
    super(message);
  }

  public AsyncException(Throwable cause) {
    super(cause);
  }
}
