package com.flowthings.client.api;

public interface SubscriptionCallback<T> {
  void onMessage(T t);
}
