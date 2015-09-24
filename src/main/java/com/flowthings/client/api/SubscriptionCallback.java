package com.flowthings.client.api;

/**
 * Receive push notifications of new Objects as they arrive
 *
 * @author matt
 * @param <T>
 *          the message type
 */
public interface SubscriptionCallback<T> {
  void onMessage(T t);
}
