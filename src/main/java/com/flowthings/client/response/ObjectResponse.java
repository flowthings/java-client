package com.flowthings.client.response;

public class ObjectResponse<T> extends Response<T> {
  public static class ERROR<T> extends ObjectResponse<T> {
    @Override
    public T get() {
      throw new IllegalStateException("Cannot get body of an error response");
    }

    @Override
    public T getBody() {
      throw new IllegalStateException("Cannot get body of an error response");
    }
  }
}
