package com.flowthings.client.domain.elements;

public interface MapLike extends Iterable<String> {
  void put(String key, Object value);

  Object get(String key);

  String getType();
}
